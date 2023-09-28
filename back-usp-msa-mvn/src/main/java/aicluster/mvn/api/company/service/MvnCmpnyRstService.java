package aicluster.mvn.api.company.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.util.LogIndvdlInfSrchUtils;
import aicluster.framework.common.util.dto.LogIndvdlInfSrch;
import aicluster.framework.security.SecurityUtils;
import aicluster.mvn.api.company.dto.MvnCmpnyPrfrmListParam;
import aicluster.mvn.api.company.dto.MvnCmpnyRstParam;
import aicluster.mvn.common.dao.UsptMvnEntrpsPfmcDao;
import aicluster.mvn.common.dao.UsptMvnEntrpsInfoDao;
import aicluster.mvn.common.dto.CmpnyPrfrmListItemDto;
import aicluster.mvn.common.dto.MvnCmpnyPrfrmDto;
import aicluster.mvn.common.dto.MvnCmpnyRsltSttusCdCountDto;
import aicluster.mvn.common.dto.MvnUserDto;
import aicluster.mvn.common.entity.UsptMvnEntrpsPfmc;
import aicluster.mvn.common.entity.UsptMvnEntrpsInfo;
import aicluster.mvn.common.entity.UsptRslt;
import aicluster.mvn.common.util.CodeExt;
import aicluster.mvn.common.util.CodeExt.validateMessageExt;
import aicluster.mvn.common.util.MvnUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.property;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.CoreUtils.webutils;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import bnet.library.util.pagination.CorePaginationParam;

@Service
public class MvnCmpnyRstService {

	@Autowired
	private UsptMvnEntrpsPfmcDao prfrmDao;

	@Autowired
	private UsptMvnEntrpsInfoDao cmpnyDao;

	@Autowired
	private MvnUtils mvnUtils;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private LogIndvdlInfSrchUtils indvdlInfSrchUtils;

	/**
	 * 입주업체성과 제출정보 목록 조회
	 *
	 * @param param : 검색조건
	 * @param pageParam : 페이징
	 * @return
	 */
	public CorePagination<CmpnyPrfrmListItemDto> getList(MvnCmpnyPrfrmListParam param, CorePaginationParam pageParam)
	{
		SecurityUtils.checkWorkerIsInsider();

		if (string.isBlank(param.getSbmsnYm())) {
			throw new InvalidationException(String.format(validateMessageExt.날짜없음, "제출년월"));
		}
		if (!date.isValidDate(param.getSbmsnYm(), "yyyyMM")) {
			throw new InvalidationException(String.format(validateMessageExt.형식오류, "제출년월", "YYYYMM"));
		}
		if (string.isNotBlank(param.getRsltSttusCd())) {
			mvnUtils.validateCode("RSLT_STTUS", param.getRsltSttusCd(), "성과상태");
		}
		if (string.isNotBlank(param.getBnoRoomNo())) {
			mvnUtils.validateBnoRoomNo(param.getBnoRoomNo());
		}


		long totalItems = prfrmDao.selectList_count( param );

		CorePaginationInfo info = new CorePaginationInfo(pageParam.getPage(), pageParam.getItemsPerPage(), totalItems);
		List<CmpnyPrfrmListItemDto> list = prfrmDao.selectList( param, info.getBeginRowNum(), info.getItemsPerPage(), totalItems );

		return new CorePagination<>(info, list);
	}

	/**
	 * 입주업체성과 제출정보 상세조회
	 *
	 * @param mvnId: 입주ID
	 * @param sbmsnYm: 제출년월
	 * @return
	 */
	public MvnCmpnyPrfrmDto get(String mvnId, String sbmsnYm)
	{
		BnMember worker = SecurityUtils.checkLogin();

		if (string.isBlank(mvnId)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "입주ID"));
		}
		if (string.isBlank(sbmsnYm)) {
			throw new InvalidationException(String.format(validateMessageExt.날짜없음, "제출년월"));
		}
		if (!date.isValidDate(sbmsnYm, "yyyyMM")) {
			throw new InvalidationException(String.format(validateMessageExt.형식오류, "제출년월", "YYYYMM"));
		}

		UsptMvnEntrpsInfo mvnCompany = cmpnyDao.select( mvnId );
		if (mvnCompany == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "입주업체 정보"));
		}
		if (!CodeExt.memberType.isInsider(worker.getMemberType()) && !string.equals(mvnCompany.getCmpnyId(), worker.getMemberId())) {
			StringBuilder authMessage = new StringBuilder(validateMessageExt.권한없음);
			authMessage.insert(0, "입주업체 성과제출정보 조회 ");
			throw new InvalidationException(authMessage.toString());
		}

		UsptMvnEntrpsPfmc cmpnyPrfrm = prfrmDao.select( mvnId, sbmsnYm );
		if (cmpnyPrfrm == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "입주업체 성과제출정보"));
		}

		MvnUserDto mvnUser = cmpnyDao.selectMvnUser( mvnId );

		MvnCmpnyPrfrmDto cmpnyPrfrmDto = new MvnCmpnyPrfrmDto();
		property.copyProperties(cmpnyPrfrmDto, cmpnyPrfrm);

		cmpnyPrfrmDto.setMvnUser(mvnUser);

		// log 정보생성
		if (!string.equals(worker.getMemberId(), mvnUser.getCmpnyId())) {
			LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
										.memberId(worker.getMemberId())
										.memberIp(webutils.getRemoteIp(request))
										.workTypeNm("조회")
										.workCn("입주업체성과 상세페이지 담당자정보 조회 업무")
										.trgterId(mvnUser.getCmpnyId())
										.email(mvnUser.getChargerEmail())
										.birthday(null)
										.mobileNo(mvnUser.getChargerMobileNo())
										.build();
			indvdlInfSrchUtils.insert(logParam);
		}

		return cmpnyPrfrmDto;
	}

	/**
	 * 입주업체성과 제출정보 등록
	 *
	 * @param param
	 */
	public void add(MvnCmpnyRstParam param)
	{
		BnMember worker = SecurityUtils.checkLogin();

		if (string.isBlank(param.getMvnId())) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "입주ID"));
		}
		if (string.isBlank(param.getSbmsnYm())) {
			throw new InvalidationException(String.format(validateMessageExt.날짜없음, "제출년월"));
		}
		if (!date.isValidDate(param.getSbmsnYm(), "yyyyMM")) {
			throw new InvalidationException(String.format(validateMessageExt.형식오류, "제출년월", "YYYYMM"));
		}

		UsptMvnEntrpsInfo mvnCompany = cmpnyDao.select( param.getMvnId() );
		if (mvnCompany == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "입주업체 정보"));
		}
		if (!string.equals(mvnCompany.getCmpnyId(), worker.getMemberId())) {
			throw new InvalidationException(String.format(validateMessageExt.행위오류, "입주업체 담당자만", "성과제출정보 등록"));
		}

		UsptRslt rslt = prfrmDao.selectRslt( param.getRsltId() );
		if (rslt == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "성과정보"));
		}

		UsptMvnEntrpsPfmc cmpnyPrfrm = prfrmDao.select( param.getMvnId(), param.getSbmsnYm() );
		if (cmpnyPrfrm != null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과있음, "해당 제출년월("+ param.getSbmsnYm() +")에 등록된 성과제출정보"));
		}

		Date now = new Date();
		cmpnyPrfrm = UsptMvnEntrpsPfmc.builder()
							.mvnId(param.getMvnId())
							.sbmsnYm(param.getSbmsnYm())
							.rsltId(param.getRsltId())
							.sbmsnDay(date.format(now, "yyyyMMdd"))
							.creatorId(worker.getMemberId())
							.createdDt(now)
							.updaterId(worker.getMemberId())
							.updatedDt(now)
							.build();

		prfrmDao.insert(cmpnyPrfrm);

	}

	/**
	 * 입주업체성과 제출정보 삭제
	 *
	 * @param mvnId: 입주ID
	 * @param sbmsnYm: 제출년월
	 */
	public void remove(String mvnId, String sbmsnYm)
	{
		BnMember worker = SecurityUtils.checkLogin();

		if (string.isBlank(mvnId)) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "입주ID"));
		}
		if (string.isBlank(sbmsnYm)) {
			throw new InvalidationException(String.format(validateMessageExt.날짜없음, "제출년월"));
		}
		if (!date.isValidDate(sbmsnYm, "yyyyMM")) {
			throw new InvalidationException(String.format(validateMessageExt.형식오류, "제출년월", "YYYYMM"));
		}

		UsptMvnEntrpsInfo mvnCompany = cmpnyDao.select( mvnId );
		if (mvnCompany == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "입주업체 정보"));
		}
		if (!string.equals(mvnCompany.getCmpnyId(), worker.getMemberId())) {
			throw new InvalidationException(String.format(validateMessageExt.행위오류, "입주업체 담당자만", "성과제출정보 삭제"));
		}

		UsptMvnEntrpsPfmc cmpnyPrfrm = prfrmDao.select( mvnId, sbmsnYm );
		if (cmpnyPrfrm == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "해당 제출년월("+ sbmsnYm +")에 등록된 성과제출정보"));
		}

		UsptRslt rslt = prfrmDao.selectRslt( cmpnyPrfrm.getRsltId() );
		if (rslt == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "입주사업 성과정보"));
		}
		if (!string.equals(rslt.getRsltSttusCd(), "PS")) {
			throw new InvalidationException(String.format(validateMessageExt.행위상태오류, "입주사업 성과정보가 제출", "삭제"));
		}

		prfrmDao.delete( mvnId, sbmsnYm );
	}

	public MvnCmpnyRsltSttusCdCountDto getStatusCount() {
		// 내부사용자 로그인 검증
		SecurityUtils.checkWorkerIsInsider();

		// 입주업체성과제출 성과상태별 건수 조회
		return prfrmDao.selectRsltSttusCd_count();
	}

}
