package aicluster.mvn.api.request.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.entity.CmmtAtchmnflGroup;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.common.util.LogIndvdlInfSrchUtils;
import aicluster.framework.common.util.dto.LogIndvdlInfSrch;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.security.Code.validateMessage;
import aicluster.framework.security.SecurityUtils;
import aicluster.mvn.api.request.dto.MvnExtDlbrtParam;
import aicluster.mvn.api.request.dto.MvnExtListParam;
import aicluster.mvn.api.request.dto.MvnExtParam;
import aicluster.mvn.api.request.dto.MvnExtStatusParam;
import aicluster.mvn.common.dao.UsptMvnEntrpsInfoDao;
import aicluster.mvn.common.dao.UsptMvnEtHistDao;
import aicluster.mvn.common.dao.UsptMvnEtReqstDao;
import aicluster.mvn.common.dao.UsptMvnFcltyInfoDao;
import aicluster.mvn.common.dto.MvnEtReqDto;
import aicluster.mvn.common.dto.MvnEtReqListItemDto;
import aicluster.mvn.common.dto.MvnEtReqStCountDto;
import aicluster.mvn.common.dto.MvnUserDto;
import aicluster.mvn.common.entity.UsptMvnEntrpsInfo;
import aicluster.mvn.common.entity.UsptMvnEtHist;
import aicluster.mvn.common.entity.UsptMvnEtReqst;
import aicluster.mvn.common.entity.UsptMvnFcltyInfo;
import aicluster.mvn.common.util.CodeExt;
import aicluster.mvn.common.util.CodeExt.criteria;
import aicluster.mvn.common.util.CodeExt.prcsWorkMessage;
import aicluster.mvn.common.util.CodeExt.prefixId;
import aicluster.mvn.common.util.CodeExt.validateMessageExt;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils.array;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.property;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.CoreUtils.webutils;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import bnet.library.util.pagination.CorePaginationParam;

@Service
public class MvnExtService {
	@Autowired
	private UsptMvnEtReqstDao mvnEtReqDao;
	@Autowired
	private UsptMvnEtHistDao mvnEtHistDao;
	@Autowired
	private UsptMvnEntrpsInfoDao mvnCmpnyDao;
	@Autowired
	private UsptMvnFcltyInfoDao mvnFcDao;
	@Autowired
	private AttachmentService atchService;
	@Autowired
	private EnvConfig config;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private LogIndvdlInfSrchUtils indvdlInfSrchUtils;

	private void validateMvnExtParam(MvnExtParam param, UsptMvnEntrpsInfo mvnCmpny)
	{
		Date now = new Date();
		int mvnEtNum = mvnCmpny.getMvnEtNum();	//입주연장횟수

		// 로그인 정보 조회
		BnMember worker = SecurityUtils.getCurrentMember();

		// 입력값 검증
		InvalidationsException errs = new InvalidationsException();
		if ( string.isBlank(param.getMvnEtEndDay()) ) {
			errs.add("mvnEtEndDay", String.format(validateMessage.날짜없음, "연장 종료일"));
		}
		if ( string.isBlank(param.getMvnEtReqCn()) ) {
			errs.add("mvnEtReqCn", String.format(validateMessage.입력없음, "연장 신청내용"));
		}
		if (errs.size() > 0) {
			throw errs;
		}

		// 수행 조건 및 권한 검증
		if (!string.equals(mvnCmpny.getCmpnyId(), worker.getMemberId())) {
			throw new InvalidationException(String.format(validateMessageExt.행위오류, "입주업체 담당자만", "연장신청"));
		}
		if (!string.equals(mvnCmpny.getMvnCmpnySt(), CodeExt.mvnCmpnySt.입주중)) {
			throw new InvalidationException(String.format(validateMessageExt.행위상태오류, "입주중", "연장신청"));
		}
		if (date.getDiffDays(now, string.toDate(mvnCmpny.getMvnEndDay())) < criteria.연장신청_기한일수) {
			throw new InvalidationException(String.format("신청시 날짜가 입주종료일 기준 %d일 전까지만 연장신청할 수 있습니다.", criteria.연장신청_기한일수));  // 입주종료일 60일 전까지만 연장신청할 수 있습니다.
		}

		// 연장 종료일자가 기존 종료일 대비 1년이내인지 검증한다.
		if (date.getDiffDays(string.toDate(mvnCmpny.getMvnEndDay()), string.toDate(param.getMvnEtEndDay())) > 365) {
			throw new InvalidationException("입주연장일은 최대 1년까지만 연장신청할 수 있습니다.");
		}
		
		if(mvnEtNum > 0) {
			String bsnsEndde = mvnCmpnyDao.selectPblancBsnsEndDt(mvnCmpny.getLastSlctnTrgetId());
			// 연장 종료일자가 기존 종료일 대비 3년이내인지 검증한다.
			if (date.getDiffDays(string.toDate(bsnsEndde), string.toDate(param.getMvnEtEndDay())) > 1095) {
				throw new InvalidationException("입주연장은 최대 3년까지 가능합니다.");
			}
		}
	}

	/**
	 * 입주연장신청 등록
	 * (입주중인 입주업체의 입주종료일이 60일 이상 남은 경우 입주업체 담당자가 등록한다.)
	 *
	 * @param param : 입주연장신청 정보(mvnId:입주ID, mvnEtEndDay:연장종료일, mvnEtReqCn:연장신청내용)
	 * @param attachments : 입주연장신청 첨부파일 목록
	 * @return UsptMvnEtReq
	 */
	public UsptMvnEtReqst add(MvnExtParam param, List<MultipartFile> attachments)
	{
		Date now = new Date();
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		// 입주업체 정보조회
		UsptMvnEntrpsInfo mvnCmpny = mvnCmpnyDao.select(param.getMvnId());
		if (mvnCmpny == null) {
			throw new InvalidationException(String.format(validateMessage.조회결과없음, "입주업체 정보"));
		}

		// 현재 진행 중인 입주연장신청 조회(진행되는 입주연장신청은 1건만 존재해야하며, 연장반려/연장승인 상태는 종료된 상태로 정의된다.)
		UsptMvnEtReqst chkMvnEtReq = mvnEtReqDao.selectGoing_MvnId(param.getMvnId());
		if (chkMvnEtReq != null) {
			throw new InvalidationException(String.format(validateMessage.조회결과있음, "진행 중인 입주연장 신청정보"));
		}

		// 입주시설 정보조회
		UsptMvnFcltyInfo mvnFc = mvnFcDao.select(mvnCmpny.getMvnFcId());
		if (mvnFc == null) {
			throw new InvalidationException(String.format(validateMessage.조회결과없음, "입주시설 정보"));
		}

		// 필수 입력 검증
		validateMvnExtParam(param, mvnCmpny);

		// 첨부파일 저장
		String attGroupId = null;
		if (attachments != null) {
			CmmtAtchmnflGroup attGroup = atchService.uploadFiles_toNewGroup(config.getDir().getUpload(), attachments, CodeExt.uploadExt.genericExt, 0L);
			attGroupId = attGroup.getAttachmentGroupId();
		}

		// 입력데이터 insert
		String newMvnEtReqId = string.getNewId(prefixId.연장신청ID);
		UsptMvnEtReqst mvnEtReq = UsptMvnEtReqst.builder()
									.mvnEtReqId(newMvnEtReqId)
									.mvnId(param.getMvnId())
									.mvnEtReqDt(now)
									.mvnEtEndDay(param.getMvnEtEndDay())
									.mvnEtReqCn(param.getMvnEtReqCn())
									.mvnEtReqSt(CodeExt.mvnEtReqSt.신청)
									.mvnEtReqStDt(now)
									.attachmentGroupId(attGroupId)
									.creatorId(worker.getMemberId())
									.createdDt(now)
									.updaterId(worker.getMemberId())
									.updatedDt(now)
									.build();
		mvnEtReqDao.insert(mvnEtReq);

		// 이력생성
		UsptMvnEtHist hist = UsptMvnEtHist.builder()
									.histId(string.getNewId(prefixId.이력ID))
									.histDt(now)
									.mvnEtReqId(newMvnEtReqId)
									.workTypeNm("신청")
									.workCn(String.format(prcsWorkMessage.처리완료, "신청"))
									.workerId(worker.getMemberId())
									.build();
		mvnEtHistDao.insert(hist);

		// 입력 건 재조회
		return mvnEtReqDao.select(newMvnEtReqId);
	}

	/**
	 * 마지막 등록한 입주연장신청 상세정보 조회
	 * (입주업체에 대한 마지막으로 등록한 입주연장신청 상세정보를 조회한다.)
	 *
	 * @param mvnId : 입주ID
	 * @return UsptMvnEtReq
	 */
	public UsptMvnEtReqst getUser(String mvnId)
	{
		// 회원정보 검증
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		// 최근 입주연장정보 조회
		return mvnEtReqDao.select_lastMvnEtReq(mvnId, worker.getMemberId());
	}

	/**
	 * 입주연장신청 정보 수정
	 * (신청취소/보완 상태인 입주연장신청 건에 대해서 입주업체 담당자가 신청정보를 수정하여 신청으로 재등록한다.)
	 *
	 * @param param : 입주연장신청 정보(mvnEtReqId:입주연장신청ID, mvnEtEndDay:연장종료일, mvnEtReqCn:연장신청내용)
	 * @param attachments : 입주연장신청 첨부파일 목록
	 * @return UsptMvnEtReq
	 */
	public UsptMvnEtReqst modify(MvnExtParam param, List<MultipartFile> attachments)
	{
		Date now = new Date();
		// 로그인 정보 검증
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		// 입주연장신청, 입주업체, 입주시설(사무실) 정보 조회
		UsptMvnEtReqst mvnEtReq = mvnEtReqDao.select( param.getMvnEtReqId() );
		if (mvnEtReq == null) {
			throw new InvalidationException(String.format(validateMessage.조회결과없음, "입주연장 신청정보"));
		}
		if ( !string.equals(mvnEtReq.getMvnId(), param.getMvnId()) ) {
			throw new InvalidationException(String.format(validateMessage.유효오류, "입주ID"));
		}
		UsptMvnEntrpsInfo mvnCmpny = mvnCmpnyDao.select( mvnEtReq.getMvnId() );
		if (mvnCmpny == null) {
			throw new InvalidationException(String.format(validateMessage.조회결과없음, "입주업체 정보"));
		}
		UsptMvnFcltyInfo mvnFc = mvnFcDao.select( mvnCmpny.getMvnFcId() );
		if (mvnFc == null) {
			throw new InvalidationException(String.format(validateMessage.조회결과없음, "입주시설(사무실) 정보"));
		}

		// 수행조건 검증
		String[] allowSt = new String[] {CodeExt.mvnEtReqSt.취소, CodeExt.mvnEtReqSt.보완};
		if ( !array.contains(allowSt, mvnEtReq.getMvnEtReqSt()) ) {
			throw new InvalidationException(String.format(validateMessageExt.행위상태오류, "신청취소 또는 보완", "수정"));
		}

		// 필수 입력값 검증
		validateMvnExtParam(param, mvnCmpny);

		// 첨부파일 업로드
		String atchGroupId = mvnEtReq.getAttachmentGroupId();
		if ( attachments != null) {
			if ( string.isNotBlank(atchGroupId) ) {
				atchService.uploadFiles_toGroup(config.getDir().getUpload(), mvnEtReq.getAttachmentGroupId(), attachments, CodeExt.uploadExt.genericExt, 0L);
			}
			else {
				CmmtAtchmnflGroup atchGroup = atchService.uploadFiles_toNewGroup(config.getDir().getUpload(), attachments, CodeExt.uploadExt.genericExt, 0L);
				atchGroupId = atchGroup.getAttachmentGroupId();
			}
		}

		// 입주연장신청 정보 수정(이전 상태가 '취소'인 경우 입주연장신청일시를 갱신한다.)
		if ( string.equals(mvnEtReq.getMvnEtReqSt(), CodeExt.mvnEtReqSt.취소) ) {
			mvnEtReq.setMvnEtReqDt(now);
		}
		mvnEtReq.setMvnEtEndDay(param.getMvnEtEndDay());
		mvnEtReq.setMvnEtReqCn(param.getMvnEtReqCn());
		mvnEtReq.setMvnEtReqSt(CodeExt.mvnEtReqSt.신청);
		mvnEtReq.setMvnEtReqStDt(now);
		mvnEtReq.setAttachmentGroupId(atchGroupId);
		mvnEtReq.setUpdaterId(worker.getMemberId());
		mvnEtReq.setUpdatedDt(now);

		mvnEtReqDao.update(mvnEtReq);

		// 입주연장신청 이력 등록
		UsptMvnEtHist hist = UsptMvnEtHist.builder()
									.histId(string.getNewId(prefixId.이력ID))
									.histDt(now)
									.mvnEtReqId(mvnEtReq.getMvnEtReqId())
									.workTypeNm("수정신청")
									.workCn(String.format(prcsWorkMessage.처리완료, "수정신청"))
									.workerId(worker.getMemberId())
									.build();
		mvnEtHistDao.insert(hist);

		// 수정정보 재조회
		return mvnEtReqDao.select( mvnEtReq.getMvnEtReqId() );
	}

	/**
	 * 입주연장신청 목록 조회
	 * (업무담당자가 조회한다.)
	 *
	 * @param param : 조회조건(mvnEtReqSt:연장신청상태, bnoRoomNo:입주호실, memberNm:업체명)
	 * @param pageParam : 페이징조건
	 * @return CorePagination<MvnEtReqListItemDto>
	 */
	public CorePagination<MvnEtReqListItemDto> getList(MvnExtListParam param, CorePaginationParam pageParam)
	{
		// 내부사용자 검증
		SecurityUtils.checkWorkerIsInsider();

		// 목록 조회
		long totalItems = mvnEtReqDao.selectList_count( param );
		CorePaginationInfo info = new CorePaginationInfo(pageParam.getPage(), pageParam.getItemsPerPage(), totalItems);
		List<MvnEtReqListItemDto> list = mvnEtReqDao.selectList( param, info.getBeginRowNum(), info.getItemsPerPage(), totalItems );

		return new CorePagination<>(info, list);
	}

	/**
	 * 입주연장신청 상세정보
	 * (업무담당자가 조회하며, 입주업체 정보와 입주시설 정보를 한번에 조회한다.)
	 *
	 * @param mvnEtReqId : 입주연장신청 ID
	 * @return MvnEtReqDto
	 */
	public MvnEtReqDto get(String mvnEtReqId)
	{
		// 내부사용자 검증
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		// 입주연장 신청 상세조회
		UsptMvnEtReqst mvnEtReq = mvnEtReqDao.select( mvnEtReqId );
		if (mvnEtReq == null) {
			throw new InvalidationException(String.format(validateMessage.조회결과없음, "입주연장 신청정보"));
		}

		// 출력 VO에 데이터 복사
		MvnEtReqDto output = new MvnEtReqDto();
		property.copyProperties(output, mvnEtReq);

		// 입주업체 및 입주시설(사무실) 정보 조회
		UsptMvnEntrpsInfo mvnCmpny = mvnCmpnyDao.select( mvnEtReq.getMvnId() );
		UsptMvnFcltyInfo mvnFc = mvnFcDao.select( mvnCmpny.getMvnFcId() );
		MvnUserDto mvnUser = mvnCmpnyDao.selectMvnUser( mvnEtReq.getMvnId() );

		output.setMvnCompany(mvnCmpny);
		output.setMvnFc(mvnFc);
		output.setMvnUser(mvnUser);

		// log 정보생성
		if (!string.equals(worker.getMemberId(), mvnUser.getCmpnyId())) {
			LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
										.memberId(worker.getMemberId())
										.memberIp(webutils.getRemoteIp(request))
										.workTypeNm("조회")
										.workCn("입주연장신청 상세페이지 담당자정보 조회 업무")
										.trgterId(mvnUser.getCmpnyId())
										.email(mvnUser.getChargerEmail())
										.birthday(null)
										.mobileNo(mvnUser.getChargerMobileNo())
										.build();
			indvdlInfSrchUtils.insert(logParam);
		}

		return output;
	}

	/**
	 * 입주연장신청 상태변경
	 * (입주업체 담당자는 신청취소를 수행할 수 있고, 업무담당자는 보완/접수완료를 수행할 수 있다.)
	 *
	 * @param param : 상태변경정보 (mvnEtReqId:입주연장신청ID, mvnEtReqSt:입주연장신청상태(취소/보완/접수완료), makeupReqCn:보완요청내용(보완 처리인 경우에만 입력된다.))
	 */
	public void modifyState(MvnExtStatusParam param)
	{
		// 로그인 정보 검증
		BnMember worker = SecurityUtils.checkLogin();

		// 입주연장 신청 상세조회
		UsptMvnEtReqst mvnEtReq = mvnEtReqDao.select( param.getMvnEtReqId() );
		if (mvnEtReq == null) {
			throw new InvalidationException(String.format(validateMessage.조회결과없음, "입주연장 신청정보"));
		}

		// 입주업체 및 입주시설(사무실) 정보 조회
		UsptMvnEntrpsInfo mvnCmpny = mvnCmpnyDao.select( mvnEtReq.getMvnId() );
		if (mvnCmpny == null) {
			throw new InvalidationException(String.format(validateMessage.조회결과없음, "입주업체 정보"));
		}

		UsptMvnFcltyInfo mvnFc = mvnFcDao.select( mvnCmpny.getMvnFcId() );
		if (mvnFc == null) {
			throw new InvalidationException(String.format(validateMessage.조회결과없음, "입주시설(사무실) 정보"));
		}

		// 입력된 상태값별 수행조건 검증
		switch (param.getMvnEtReqSt()) {
			case CodeExt.mvnEtReqSt.보완		:
			case CodeExt.mvnEtReqSt.접수완료	:
				// 내부사용자 검증
				SecurityUtils.checkWorkerIsInsider();

				// 변경전 상태 검증
				if ( !string.equals(mvnEtReq.getMvnEtReqSt(), CodeExt.mvnEtReqSt.신청)) {
					throw new InvalidationException(String.format(validateMessageExt.행위상태오류, "신청", "상태 변경"));
				}

				// 보완 필수값 검증
				if ( string.equals(param.getMvnEtReqSt(), CodeExt.mvnEtReqSt.보완) && string.isBlank(param.getMakeupReqCn())) {
					throw new InvalidationException(String.format(validateMessage.입력없음, "보완요청내용"));
				}
				break;

			case CodeExt.mvnEtReqSt.취소		:
				// 업체담당자 검증
				if ( !string.equals(worker.getMemberId(), mvnCmpny.getCmpnyId()) ) {
					throw new InvalidationException(String.format(validateMessageExt.행위오류, "업체담당자만", "신청 취소"));
				}

				// 변경전 상태 검증
				if ( !string.equals(mvnEtReq.getMvnEtReqSt(), CodeExt.mvnEtReqSt.신청)) {
					throw new InvalidationException(String.format(validateMessageExt.행위상태오류, "신청", "신청 취소"));
				}
				break;

			case CodeExt.mvnEtReqSt.연장반려	:
			case CodeExt.mvnEtReqSt.연장승인	:
				throw new InvalidationException("연장승인/연장반려는 심의결과등록으로 진행하세요.");
			default			:  // 그 외의 경우
				throw new InvalidationException(String.format(validateMessage.유효오류, "입주연장 상태코드"));
		}

		// 입주연장신청정보 상태값 데이터 변경
		Date now = new Date();
		mvnEtReq.setMvnEtReqSt(param.getMvnEtReqSt());
		mvnEtReq.setMvnEtReqStDt(now);
		if ( string.equals(param.getMvnEtReqSt(), CodeExt.mvnEtReqSt.보완) ) {
			mvnEtReq.setMakeupReqCn(param.getMakeupReqCn());
		}
		else {
			mvnEtReq.setMakeupReqCn(null);
		}
		mvnEtReq.setUpdaterId(worker.getMemberId());
		mvnEtReq.setUpdatedDt(now);

		// 입주연장신청정보 갱신
		mvnEtReqDao.update(mvnEtReq);

		// 입주연장신청정보 이력 등록
		UsptMvnEtHist hist = UsptMvnEtHist.builder()
									.histId(string.getNewId(prefixId.이력ID))
									.histDt(now)
									.mvnEtReqId(mvnEtReq.getMvnEtReqId())
									.workerId(worker.getMemberId())
									.build();
		switch (param.getMvnEtReqSt()) {
			case CodeExt.mvnEtReqSt.보완		:
				hist.setWorkTypeNm("보완");
				hist.setWorkCn(String.format(prcsWorkMessage.처리완료, "보완요청"));
				break;

			case CodeExt.mvnEtReqSt.접수완료	:
				hist.setWorkTypeNm("접수");
				hist.setWorkCn(String.format(prcsWorkMessage.처리완료, "접수완료"));
				break;

			case CodeExt.mvnEtReqSt.취소		:
				hist.setWorkTypeNm("신청취소");
				hist.setWorkCn(String.format(prcsWorkMessage.처리완료, "신청취소"));
				break;
		}

		mvnEtHistDao.insert(hist);
	}

	/**
	 * 심의결과 등록
	 * (업무담당자가 심의결과를 등록하고, 연장반려/연장승인으로 상태변경한다.)
	 *
	 * @param param : 심의결과 정보
	 * @param attachments : 심의결과 첨부파일 목록
	 */
	public void modifyDlbrt(MvnExtDlbrtParam param, List<MultipartFile> attachments)
	{
		// 내부사용자 검증
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		// 입주연장 신청 상세조회
		UsptMvnEtReqst mvnEtReq = mvnEtReqDao.select( param.getMvnEtReqId() );
		if (mvnEtReq == null) {
			throw new InvalidationException(String.format(validateMessage.조회결과없음, "입주연장 신청정보"));
		}

		// 입주업체 및 입주시설(사무실) 정보 조회
		UsptMvnEntrpsInfo mvnCmpny = mvnCmpnyDao.select( mvnEtReq.getMvnId() );
		if (mvnCmpny == null) {
			throw new InvalidationException(String.format(validateMessage.조회결과없음, "입주업체 정보"));
		}

		UsptMvnFcltyInfo mvnFc = mvnFcDao.select( mvnCmpny.getMvnFcId() );
		if (mvnFc == null) {
			throw new InvalidationException(String.format(validateMessage.조회결과없음, "입주시설(사무실) 정보"));
		}

		// 수행조건 검증
		if ( !string.equals(mvnEtReq.getMvnEtReqSt(), CodeExt.mvnEtReqSt.접수완료) ) {
			throw new InvalidationException(String.format(validateMessageExt.행위상태오류, "접수완료", "심의결과 등록"));
		}

		// 입력값 검증
		InvalidationsException errs = new InvalidationsException();
		String[] allowSt = new String[] {CodeExt.mvnEtReqSt.연장반려, CodeExt.mvnEtReqSt.연장승인};
		if ( !array.contains(allowSt, param.getMvnEtReqSt()) ) {
			errs.add("mvnEtReqSt", String.format(validateMessageExt.유효오류, "심의결과 상태코드"));
		}
		if ( param.getDlbrtDay() == null ) {
			errs.add("dlbrtDay", String.format(validateMessageExt.날짜없음, "심의일"));
		}
		if ( string.isBlank(param.getDlbrtResultDtlCn()) ) {
			errs.add("dlbrtResultDtlCn", String.format(validateMessage.입력없음, "심의결과 상세내용"));
		}
		if ( string.equals(param.getMvnEtReqSt(), CodeExt.mvnEtReqSt.연장승인) && param.getDlbrtAprvEndDay()==null ) {
			errs.add("dlbrtAprvEndDay", String.format(validateMessageExt.날짜없음, "연장승인 종료일"));
		}

		if ( errs.size() > 0 ) {
			throw errs;
		}

		// 첨부파일 등록
		String attGroupId = null;
		if (attachments != null) {
			CmmtAtchmnflGroup attGroup = atchService.uploadFiles_toNewGroup(config.getDir().getUpload(), attachments, CodeExt.uploadExt.genericExt, 0L);
			attGroupId = attGroup.getAttachmentGroupId();
		}

		// 심의결과 데이터 수정
		Date now = new Date();
		mvnEtReq.setDlbrtDay(date.format(param.getDlbrtDay(), "yyyyMMdd"));
		mvnEtReq.setDlbrtAprvEndDay(date.format(param.getDlbrtAprvEndDay(), "yyyyMMdd"));
		mvnEtReq.setDlbrtResultDtlCn(param.getDlbrtResultDtlCn());
		mvnEtReq.setDlbrtAtchGroupId(attGroupId);
		mvnEtReq.setMvnEtReqSt(param.getMvnEtReqSt());
		mvnEtReq.setMvnEtReqStDt(now);
		mvnEtReq.setUpdaterId(worker.getMemberId());
		mvnEtReq.setUpdatedDt(now);

		// 심의결과 등록
		mvnEtReqDao.update(mvnEtReq);

		// 연장승인인 경우 심의승인 종료일로 입주업체 입주종료일을 갱신한다.
		if ( string.equals(param.getMvnEtReqSt(), CodeExt.mvnEtReqSt.연장승인) ) {
			mvnCmpny.setMvnEndDay(date.format(param.getDlbrtAprvEndDay(), "yyyyMMdd"));
			mvnCmpny.setMvnEtNum(mvnCmpny.getMvnEtNum()+1);
			mvnCmpny.setUpdaterId(worker.getMemberId());
			mvnCmpny.setUpdatedDt(now);

			mvnCmpnyDao.update(mvnCmpny);
		}

		// 입주연장신청 이력 등록
		UsptMvnEtHist hist = UsptMvnEtHist.builder()
									.histId(string.getNewId(prefixId.이력ID))
									.histDt(now)
									.mvnEtReqId(mvnEtReq.getMvnEtReqId())
									.workerId(worker.getMemberId())
									.build();

		switch (param.getMvnEtReqSt()) {
			case CodeExt.mvnEtReqSt.연장반려 :
				hist.setWorkTypeNm("반려");
				hist.setWorkCn(String.format(prcsWorkMessage.처리완료, "연장반려"));
				break;

			case CodeExt.mvnEtReqSt.연장승인 :
				hist.setWorkTypeNm("승인");
				hist.setWorkCn(String.format(prcsWorkMessage.처리완료, "연장승인"));
				break;
		}

		mvnEtHistDao.insert(hist);

	}

	/**
	 * 입주연장신청 처리이력 조회
	 * (업무담당자가 입주연장신청 건의 이력을 조회한다.)
	 *
	 * @param mvnEtReqId : 입주연장신청ID
	 * @return List<UsptMvnEtHist>
	 */
	public JsonList<UsptMvnEtHist> getHist(String mvnEtReqId)
	{
		// 내부사용자 검증
		SecurityUtils.checkWorkerIsInsider();

		// 입주연장 신청 상세조회
		UsptMvnEtReqst mvnEtReq = mvnEtReqDao.select( mvnEtReqId );
		if (mvnEtReq == null) {
			throw new InvalidationException(String.format(validateMessage.조회결과없음, "입주연장 신청정보"));
		}

		// 이력정보 목록조회
		List<UsptMvnEtHist> list = mvnEtHistDao.selectList(mvnEtReqId);

		return new JsonList<>(list);
	}

	/**
	 * 로그인 검증 후 입주연장신청 정보 조회
	 *
	 * @param mvnEtReqId : 입주연장신청ID
	 * @return UsptMvnEtReq
	 */
	public UsptMvnEtReqst select_chkLogin(String mvnEtReqId)
	{
		// 로그인정보 검증
		SecurityUtils.checkLogin();

		// 입주연장 신청 상세조회
		UsptMvnEtReqst mvnEtReq = mvnEtReqDao.select( mvnEtReqId );
		if (mvnEtReq == null) {
			throw new InvalidationException(String.format(validateMessage.조회결과없음, "입주연장 신청정보"));
		}

		return mvnEtReq;
	}

	/**
	 * 입주연장신청 첨부파일 정보 조회
	 *
	 * @param mvnEtReqId : 입주연장신청ID
	 * @param attachmentId : 첨부파일ID
	 * @return CmmtAtchmnfl
	 */
	public CmmtAtchmnfl selectReqFile(String mvnEtReqId, String attachmentId)
	{
		// 입주연장 신청 상세조회
		UsptMvnEtReqst mvnEtReq = this.select_chkLogin( mvnEtReqId );

		// 첨부파일 정보 조회
		CmmtAtchmnfl downloadAtch = atchService.getFileInfo(attachmentId);

		// 첨부파일 검증
		if ( downloadAtch == null ) {
			throw new InvalidationException(String.format(validateMessage.조회결과없음, "입주연장신청 첨부파일정보"));
		}
		if ( !string.equals(mvnEtReq.getAttachmentGroupId(), downloadAtch.getAttachmentGroupId()) ) {
			throw new InvalidationException(String.format(validateMessage.유효오류, "입주연장신청 첨부파일ID"));
		}

		return downloadAtch;
	}

	/**
	 * 입주연장신청 첨부파일 삭제
	 * (신청취소/보안 상태인 입주연장신청 정보의 첨부파일을 입주업체 담당자가 삭제한다.)
	 *
	 * @param mvnEtReqId : 입주연장신청ID
	 * @param attachmentId : 첨부파일ID
	 */
	public void removeReqFile(String mvnEtReqId, String attachmentId)
	{
		// 입주연장 신청 상세조회
		UsptMvnEtReqst mvnEtReq = this.select_chkLogin( mvnEtReqId );

		// 회원정보 검증
		BnMember worker = SecurityUtils.checkWorkerIsMember();

		// 입주업체 정보 조회
		UsptMvnEntrpsInfo mvnCmpny = mvnCmpnyDao.select( mvnEtReq.getMvnId() );
		if (mvnCmpny == null) {
			throw new InvalidationException(String.format(validateMessage.조회결과없음, "입주업체 정보"));
		}

		// 수행조건 검증
		String[] allowSt = new String[] {CodeExt.mvnEtReqSt.취소, CodeExt.mvnEtReqSt.보완};
		if ( !array.contains(allowSt, mvnEtReq.getMvnEtReqSt()) ) {
			throw new InvalidationException(String.format(validateMessageExt.행위상태오류, "신청취소 또는 보완", "첨부파일을 삭제"));
		}
		if ( !string.equals(worker.getMemberId(), mvnCmpny.getCmpnyId()) ) {
			throw new InvalidationException(String.format(validateMessageExt.행위오류, "입주업체 담당자만", "첨부파일을 삭제"));
		}

		// 첨부파일 정보 조회
		CmmtAtchmnfl removeAtch = atchService.getFileInfo(attachmentId);

		// 첨부파일 검증
		if ( removeAtch == null ) {
			throw new InvalidationException(String.format(validateMessage.조회결과없음, "입주연장신청 첨부파일정보"));
		}
		if ( !string.equals(mvnEtReq.getAttachmentGroupId(), removeAtch.getAttachmentGroupId()) ) {
			throw new InvalidationException(String.format(validateMessage.유효오류, "입주연장신청 첨부파일ID"));
		}

		// 첨부파일 삭제
		boolean attExists = atchService.removeFile(config.getDir().getUpload(), removeAtch.getAttachmentId());

		// 첨부파일 목록이 없다면 업무테이블의 첨부파일 GroupID null 처리
		if (!attExists) {
			mvnEtReq.setAttachmentGroupId(null);
			mvnEtReq.setUpdaterId(worker.getMemberId());
			mvnEtReq.setUpdatedDt(new Date());

			mvnEtReqDao.update(mvnEtReq);
		}
	}

	/**
	 * 심의결과 첨부파일 정보 조회
	 *
	 * @param mvnEtReqId : 입주연장신청ID
	 * @param attachmentId : 첨부파일ID
	 * @return CmmtAtchmnfl
	 */
	public CmmtAtchmnfl selectDlbrtFile(String mvnEtReqId, String attachmentId)
	{
		// 입주연장 신청 상세조회
		UsptMvnEtReqst mvnEtReq = this.select_chkLogin( mvnEtReqId );

		// 첨부파일 정보 조회
		CmmtAtchmnfl downloadAtch = atchService.getFileInfo(attachmentId);

		// 첨부파일 검증
		if ( downloadAtch == null ) {
			throw new InvalidationException(String.format(validateMessage.조회결과없음, "심의결과 첨부파일정보"));
		}
		if ( !string.equals(mvnEtReq.getDlbrtAtchGroupId(), downloadAtch.getAttachmentGroupId()) ) {
			throw new InvalidationException(String.format(validateMessage.유효오류, "심의결과 첨부파일ID"));
		}

		return downloadAtch;
	}

	/**
	 * 심의결과 첨부파일 삭제
	 * (연장반려 또는 연장승인 상태인 입주연장신청 정보의 첨부파일을 업무담당자가 삭제한다.)
	 *
	 * @param mvnEtReqId : 입주연장신청ID
	 * @param attachmentId : 첨부파일ID
	 */
	public void removeDlbrtFile(String mvnEtReqId, String attachmentId)
	{
		// 입주연장 신청 상세조회
		UsptMvnEtReqst mvnEtReq = this.select_chkLogin( mvnEtReqId );

		// 내부사용자 검증
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		// 수행조건 검증
		String[] allowSt = new String[] {CodeExt.mvnEtReqSt.연장승인, CodeExt.mvnEtReqSt.연장반려};
		if ( !array.contains(allowSt, mvnEtReq.getMvnEtReqSt()) ) {
			throw new InvalidationException(String.format(validateMessageExt.행위상태오류, "연장승인 또는 연장반려", " 심의결과 첨부파일을 삭제"));
		}

		// 첨부파일 정보 조회
		CmmtAtchmnfl removeAtch = atchService.getFileInfo(attachmentId);

		// 첨부파일 검증
		if ( removeAtch == null ) {
			throw new InvalidationException(String.format(validateMessage.조회결과없음, "심의결과 첨부파일정보"));
		}
		if ( !string.equals(mvnEtReq.getDlbrtAtchGroupId(), removeAtch.getAttachmentGroupId()) ) {
			throw new InvalidationException(String.format(validateMessage.유효오류, "심의결과 첨부파일ID"));
		}

		// 첨부파일 삭제
		boolean attExists = atchService.removeFile(config.getDir().getUpload(), removeAtch.getAttachmentId());

		// 첨부파일 목록이 없다면 업무테이블의 첨부파일 GroupID null 처리
		if (!attExists) {
			mvnEtReq.setDlbrtAtchGroupId(null);
			mvnEtReq.setUpdaterId(worker.getMemberId());
			mvnEtReq.setUpdatedDt(new Date());

			mvnEtReqDao.update(mvnEtReq);
		}
	}

	/**
	 * 입주연장신청 첨부파일 다운로드용 정보 조회
	 *
	 * @param mvnEtReqId : 입주연장신청ID
	 * @return
	 */
	public MvnEtReqDto getFilesDownloadInfo(String mvnEtReqId)
	{
		// 입주연장 신청 상세조회
		UsptMvnEtReqst mvnEtReq = mvnEtReqDao.select( mvnEtReqId );
		if (mvnEtReq == null) {
			throw new InvalidationException(String.format(validateMessage.조회결과없음, "입주연장 신청정보"));
		}

		// 출력 VO에 데이터 복사
		MvnEtReqDto output = new MvnEtReqDto();
		property.copyProperties(output, mvnEtReq);

		// 입주업체 및 입주시설(사무실) 정보 조회
		UsptMvnEntrpsInfo mvnCmpny = mvnCmpnyDao.select( mvnEtReq.getMvnId() );
		output.setMvnCompany(mvnCmpny);

		return output;
	}

	public MvnEtReqStCountDto getStatusCount() {
		// 내부사용자 로그인 검증
		SecurityUtils.checkWorkerIsInsider();

		// 입주연장신청상태별 건수 조회
		return mvnEtReqDao.selectMvnEtReqSt_count();
	}

}
