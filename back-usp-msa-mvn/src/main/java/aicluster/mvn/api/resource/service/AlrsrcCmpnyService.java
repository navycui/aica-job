package aicluster.mvn.api.resource.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.dao.FwCodeDao;
import aicluster.framework.common.dao.FwUserDao;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CodeDto;
import aicluster.framework.common.entity.UserDto;
import aicluster.framework.security.SecurityUtils;
import aicluster.mvn.api.resource.dto.AlrsrcCmpnyBsnsParam;
import aicluster.mvn.api.resource.dto.AlrsrcCmpnyInsListItem;
import aicluster.mvn.api.resource.dto.AlrsrcCmpnyListParam;
import aicluster.mvn.api.resource.dto.AlrsrcCmpnyPeriodParam;
import aicluster.mvn.api.resource.dto.AlrsrcCmpnyStatusParam;
import aicluster.mvn.api.resource.dto.AlrsrcDstbInsListItem;
import aicluster.mvn.common.dao.UsptResrceAsgnDstbDao;
import aicluster.mvn.common.dao.UsptResrceAsgnDstbHistDao;
import aicluster.mvn.common.dao.UsptResrceAsgnEntrpsDao;
import aicluster.mvn.common.dao.UsptResrceAsgnHistDao;
import aicluster.mvn.common.dao.UsptResrceInvntryInfoDao;
import aicluster.mvn.common.dao.UsptResrceInvntryInfoHistDao;
import aicluster.mvn.common.dto.AlrsrcCmpnyListItemDto;
import aicluster.mvn.common.dto.AlrsrcCmpnySlctnDto;
import aicluster.mvn.common.dto.AlrsrcDstbHistDto;
import aicluster.mvn.common.dto.AlrsrcFirstDstbDto;
import aicluster.mvn.common.entity.UsptResrceAsgnDstb;
import aicluster.mvn.common.entity.UsptResrceAsgnDstbHist;
import aicluster.mvn.common.entity.UsptResrceAsgnEntrps;
import aicluster.mvn.common.entity.UsptResrceAsgnHist;
import aicluster.mvn.common.entity.UsptResrceInvntryInfo;
import aicluster.mvn.common.entity.UsptResrceInvntryInfoHist;
import aicluster.mvn.common.util.CodeExt;
import aicluster.mvn.common.util.CodeExt.alrsrcSt;
import aicluster.mvn.common.util.CodeExt.prefixId;
import aicluster.mvn.common.util.CodeExt.validateMessageExt;
import aicluster.mvn.common.util.MvnUtils;
import aicluster.mvn.common.util.MvnUtils.invtVsReqDstb;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils.array;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.property;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import bnet.library.util.pagination.CorePaginationParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AlrsrcCmpnyService {

	@Autowired
	private UsptResrceAsgnEntrpsDao cmpnyDao;
	@Autowired
	private UsptResrceAsgnHistDao histDao;
	@Autowired
	private UsptResrceAsgnDstbDao dstbDao;
	@Autowired
	private UsptResrceAsgnDstbHistDao dstbHistDao;
	@Autowired
	private UsptResrceInvntryInfoDao fninfDao;
	@Autowired
	private UsptResrceInvntryInfoHistDao fninfHistDao;
	@Autowired
	private FwUserDao userDao;
	@Autowired
	private FwCodeDao codeDao;

	@Autowired
	private MvnUtils mvnUtils;

	private final String[] SEARCH_TYPE = {"CMPNY_NM", "RECEIPT_NO", "PBLANC_NM"};

	/**
	 * 자원할당업체 목록 조회
	 *
	 * @param param : 검색조건
	 * @param pageParam : 페이징
	 * @return 목록 데이터
	 */
	public CorePagination<AlrsrcCmpnyListItemDto> getList(AlrsrcCmpnyListParam param, CorePaginationParam pageParam)
	{
		SecurityUtils.checkWorkerIsInsider();

		// 검색조건 검증
		if ( string.isBlank(param.getAlrsrcBgngDay()) || string.isBlank(param.getAlrsrcEndDay()) ) {
			throw new InvalidationException(String.format(validateMessageExt.선택없음, "조회기간"));
		}
		if ( string.isNotBlank(param.getBsnsYear()) && !date.isValidDate(param.getBsnsYear(), "yyyy") ) {
			throw new InvalidationException(String.format(validateMessageExt.형식오류, "사업연도", "YYYY"));
		}
		if ( string.isNotBlank(param.getAlrsrcSt()) && !mvnUtils.isValideCode(alrsrcSt.CODE_GROUP, param.getAlrsrcSt()) ) {
			throw new InvalidationException(String.format(validateMessageExt.유효오류, "자원할당상태"));
		}
		if ( string.isNotBlank(param.getSearchType()) && !array.contains(SEARCH_TYPE, param.getSearchType()) ) {
			throw new InvalidationException(String.format(validateMessageExt.유효오류, "검색유형"));
		}

		// 목록 조회
		long totalItems = cmpnyDao.selectList_count(param);
		CorePaginationInfo info = new CorePaginationInfo(pageParam.getPage(), pageParam.getItemsPerPage(), totalItems);
		List<AlrsrcCmpnyListItemDto> list = cmpnyDao.selectList(param, info.getBeginRowNum(), info.getItemsPerPage(), info.getTotalItems());

		return new CorePagination<>(info, list);
	}

	/**
	 * 자원할당업체 상세 조회
	 *
	 * @param alrsrcId
	 * @return 자원할당업체 정보
	 */
	public UsptResrceAsgnEntrps get(String alrsrcId)
	{
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		UsptResrceAsgnEntrps cmpny = cmpnyDao.select(alrsrcId);
		if (cmpny == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "자원할당업체 정보"));
		}

		// 자원재고 목록 조회
		List<UsptResrceInvntryInfo> fninfList = fninfDao.selectList();
		if (fninfList.isEmpty()) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "자원재고 정보"));
		}
		cmpny.setAlrsrcFninfList(fninfList);

		// 자원배분 목록 조회
		List<UsptResrceAsgnDstb> dstbList = dstbDao.selectList(alrsrcId);
		cmpny.setAlrsrcDstbList(dstbList);

		// 자원배분 이력 조회
		List<AlrsrcDstbHistDto> dstbHist = dstbHistDao.selectList(alrsrcId);
		cmpny.setAlrsrcDstbHist(dstbHist);

		// log 정보생성(마스킹 출력이므로 이력 생성하지 않는다.)
		if (!string.equals(worker.getMemberId(), cmpny.getCmpnyId())) {
//			LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
//										.memberId(worker.getMemberId())
//										.memberIp(webutils.getRemoteIp(request))
//										.workTypeNm("조회")
//										.workCn("자원할당업체관리 상세페이지 담당자정보 조회 업무")
//										.trgterId(cmpny.getCmpnyId())
//										.email(cmpny.getEmail())
//										.birthday(null)
//										.mobileNo(cmpny.getMobileNo())
//										.build();
//			indvdlInfSrchUtils.insert(logParam);
		}

		return cmpny;
	}

	/**
	 * 자원할당업체 일괄등록
	 *
	 * @param param : 사업관리 최종선정 업체 목록
	 */
	public void add(AlrsrcCmpnyBsnsParam param)
	{
		Date now = new Date();
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		// 입력 데이터 검증
		InvalidationsException errs = new InvalidationsException();

		if (string.isBlank(param.getEvlLastSlctnId())) {
			log.error(String.format(validateMessageExt.입력없음, "평가최종선정ID"));
			errs.add("evlLastSlctnId", String.format(validateMessageExt.입력없음, "평가최종선정ID"));
		}

		List<AlrsrcCmpnySlctnDto> bsnsSlctnList = cmpnyDao.selectList_slctnId(param.getEvlLastSlctnId());

		List<AlrsrcCmpnyInsListItem> alrsrcCmpnyInsList = param.getAlrsrcCmpnyInsList();
		if (alrsrcCmpnyInsList.isEmpty()) {
			log.error(String.format(validateMessageExt.입력없음, "최종선정업체 정보"));
			errs.add("alrsrcCmpnyInsList", String.format(validateMessageExt.입력없음, "최종선정업체 정보"));
		}

		// 자원재고 대비 신청수량 목록 초기화
		mvnUtils.clearInvtList();

		int rowNum = 1;
		for (AlrsrcCmpnyInsListItem alrsrcCmpnyIns : alrsrcCmpnyInsList) {
			if (string.isBlank(alrsrcCmpnyIns.getLastSlctnTrgetId())) {
				log.error(String.format("[%d]행의 " + validateMessageExt.입력없음, rowNum, "최종선정대상ID"));
				errs.add("alrsrcCmpnyInsList.lastSlctnTrgetId", String.format("[%d]행의 " + validateMessageExt.입력없음, rowNum, "최종선정대상ID"));
			}

			if (string.isBlank(alrsrcCmpnyIns.getBsnsSlctnId())) {
				log.error(String.format("[%d]행의 " + validateMessageExt.입력없음, rowNum, "선정대상ID"));
				errs.add("alrsrcCmpnyInsList.bsnsSlctnId", String.format("[%d]행의 " + validateMessageExt.입력없음, rowNum, "선정대상ID"));
			}

			// 최정선정대상ID 가 이미 등록되어 있는지 검증한다.
			boolean isDupBsns = bsnsSlctnList.stream()
									.filter(obj -> obj.getLastSlctnTrgetId().equals(alrsrcCmpnyIns.getLastSlctnTrgetId()))
									.findFirst().isPresent();
			if (isDupBsns) {
				log.error(String.format("[%d]행의 " + validateMessageExt.조회결과있음, rowNum, "최종선정대상자"));
				errs.add("alrsrcCmpnyInsList.bsnsSlctnId", String.format("[%d]행의 " + validateMessageExt.조회결과있음, rowNum, "최종선정대상자"));
			}

			if (string.isBlank(alrsrcCmpnyIns.getReceiptNo())) {
				log.error(String.format("[%d]행의 " + validateMessageExt.입력없음, rowNum, "접수번호"));
				errs.add("alrsrcCmpnyInsList.receiptNo", String.format("[%d]행의 " + validateMessageExt.입력없음, rowNum, "접수번호"));
			}
			if (string.isBlank(alrsrcCmpnyIns.getMemberId())) {
				log.error(String.format("[%d]행의 " + validateMessageExt.입력없음, rowNum, "회원ID"));
				errs.add("alrsrcCmpnyInsList.memberId", String.format("[%d]행의 " + validateMessageExt.입력없음, rowNum, "회원ID"));
			}
			if (string.isBlank(alrsrcCmpnyIns.getBsnsBgnde())) {
				log.error(String.format("[%d]행의 " + validateMessageExt.입력없음, rowNum, "사업시작일"));
				errs.add("alrsrcCmpnyInsList.bsnsBgnde", String.format("[%d]행의 " + validateMessageExt.입력없음, rowNum, "사업시작일"));
			}
			if (string.isBlank(alrsrcCmpnyIns.getBsnsEndde())) {
				log.error(String.format("[%d]행의 " + validateMessageExt.입력없음, rowNum, "사업종료일"));
				errs.add("alrsrcCmpnyInsList.bsnsEndde", String.format("[%d]행의 " + validateMessageExt.입력없음, rowNum, "사업종료일"));
			}

			if (date.compareDay(string.toDate(alrsrcCmpnyIns.getBsnsBgnde()), string.toDate(alrsrcCmpnyIns.getBsnsEndde())) > -1) {
				log.error(String.format("[%d]행의 " + validateMessageExt.일시_크거나같은비교오류, rowNum, "사업시작일", "사업종료일", "날짜"));
				errs.add("alrsrcCmpnyInsList.bsnsBgnde", String.format("[%d]행의 " + validateMessageExt.일시_크거나같은비교오류, rowNum, "사업시작일", "사업종료일", "날짜"));
			}
			if (date.compareDay(now, string.toDate(alrsrcCmpnyIns.getBsnsEndde())) > -1) {
				log.error(String.format("[%d]행의 " + validateMessageExt.일시_작거나같은비교오류, rowNum, "사업종료일", "현재일", "날짜"));
				errs.add("alrsrcCmpnyInsList.bsnsEndde", String.format("[%d]행의 " + validateMessageExt.일시_작거나같은비교오류, rowNum, "사업종료일", "현재일", "날짜"));
			}

			// 자원할당 요청 목록 검증
			List<AlrsrcDstbInsListItem> alrsrcDstbInsList = alrsrcCmpnyIns.getAlrsrcDstbInsList();
			if (alrsrcDstbInsList.isEmpty()) {
				log.error(String.format("[%d]행의 " + validateMessageExt.입력없음, rowNum, "자원할당정보"));
				errs.add("alrsrcCmpnyInsList.alrsrcDstbInsList", String.format("[%d]행의 " + validateMessageExt.입력없음, rowNum, "자원할당정보"));
			}

			// 사업공고 선정대상자 정보 검증
			UserDto user = userDao.select(alrsrcCmpnyIns.getMemberId());
			if (user == null) {
				log.error(String.format("[%d]행의 " + validateMessageExt.조회결과없음, rowNum, "사업공고 선정대상자 정보"));
				errs.add("alrsrcCmpnyInsList.memberId", String.format("[%d]행의 " + validateMessageExt.조회결과없음, rowNum, "사업공고 선정대상자 정보"));
			}

			// 자원재고정보 검증 및 재고수량 대비 할당신청수량 검증
			for (AlrsrcDstbInsListItem alrsrcDstbIns : alrsrcDstbInsList) {
				if (!mvnUtils.isInvtRsrcId(alrsrcDstbIns.getRsrcId())) {
					log.error(String.format("[%d]행의 " + validateMessageExt.포함불가, rowNum, "자원ID"));
					errs.add("alrsrcCmpnyInsList.alrsrcDstbInsList", String.format("[%d]행의 자원할당 목록에 " + validateMessageExt.포함불가, rowNum, "자원ID"));
				}
				else {
					int rsrcReqDstbQy = Long.valueOf(alrsrcDstbIns.getRsrcDstbQy()).intValue();
					if (mvnUtils.isInvtQyExcessYnForAccmltQy(alrsrcDstbIns.getRsrcId(), rsrcReqDstbQy)) {
						String rsrcGroupNm = mvnUtils.getRsrcGroupNm(alrsrcDstbIns.getRsrcId());
						String rsrcTypeNm = mvnUtils.getRsrcTypeNm(alrsrcDstbIns.getRsrcId());

						log.error(String.format("[%s]의 [%s] 자원유형의 자원할당 신청수량이 재고수량를 초과하였습니다.", rsrcGroupNm, rsrcTypeNm));
						errs.add("alrsrcCmpnyInsList.alrsrcDstbInsList", String.format("[%s]의 [%s] 자원유형의 자원할당 신청수량이 재고수량를 초과하였습니다.", rsrcGroupNm, rsrcTypeNm));
					}
				}
			}

			rowNum++;
		}

		if (errs.size() > 0) {
			throw errs;
		}

		// 입력 데이터 VO 목록 생성
		List<UsptResrceAsgnEntrps> insCmpnyList = new ArrayList<>();
		List<UsptResrceAsgnDstb> insDstbList = new ArrayList<>();
		List<UsptResrceAsgnDstbHist> insDstbHistList = new ArrayList<>();
		for (AlrsrcCmpnyInsListItem alrsrcCmpnyIns : alrsrcCmpnyInsList) {
			String alrsrcId = string.getNewId(prefixId.자원할당ID);

			UsptResrceAsgnEntrps cmpny = UsptResrceAsgnEntrps.builder().alrsrcId(alrsrcId).build();
			cmpny.setEvlLastSlctnId(param.getEvlLastSlctnId());
			cmpny.setLastSlctnTrgetId(alrsrcCmpnyIns.getLastSlctnTrgetId());
			cmpny.setBsnsSlctnId(alrsrcCmpnyIns.getBsnsSlctnId());
			cmpny.setReceiptNo(alrsrcCmpnyIns.getReceiptNo());
			cmpny.setCmpnyId(alrsrcCmpnyIns.getMemberId());
			cmpny.setAlrsrcBgngDay(alrsrcCmpnyIns.getBsnsBgnde());
			cmpny.setAlrsrcEndDay(alrsrcCmpnyIns.getBsnsEndde());

			if (date.compareDay(now, string.toDate(alrsrcCmpnyIns.getBsnsBgnde())) < 0) {
				cmpny.setAlrsrcSt(CodeExt.alrsrcSt.이용대기);
			}
			else {
				cmpny.setAlrsrcSt(CodeExt.alrsrcSt.이용중);
			}
			cmpny.setAlrsrcStDt(now);

			cmpny.setCreatorId(worker.getMemberId());
			cmpny.setCreatedDt(now);
			cmpny.setUpdaterId(worker.getMemberId());
			cmpny.setUpdatedDt(now);

			insCmpnyList.add(cmpny);

			List<AlrsrcDstbInsListItem> alrsrcDstbInsList = alrsrcCmpnyIns.getAlrsrcDstbInsList();
			for (AlrsrcDstbInsListItem alrsrcDstbIns : alrsrcDstbInsList) {
				insDstbList.add(UsptResrceAsgnDstb.builder()
								.alrsrcId(alrsrcId)
								.rsrcId(alrsrcDstbIns.getRsrcId())
								.rsrcUseYn(alrsrcDstbIns.getRsrcUseYn())
								.rsrcDstbQy(Long.valueOf(alrsrcDstbIns.getRsrcDstbQy()).intValue())
								.rsrcDstbCn(alrsrcDstbIns.getRsrcDstbCn())
								.creatorId(worker.getMemberId())
								.createdDt(now)
								.updaterId(worker.getMemberId())
								.updatedDt(now)
								.build());

				insDstbHistList.add(UsptResrceAsgnDstbHist.builder()
								.histId(string.getNewId(prefixId.이력ID))
								.histDt(now)
								.firstDstbYn(true)
								.alrsrcId(alrsrcId)
								.rsrcId(alrsrcDstbIns.getRsrcId())
								.rsrcUseYn(alrsrcDstbIns.getRsrcUseYn())
								.rsrcDstbQy(Long.valueOf(alrsrcDstbIns.getRsrcDstbQy()).intValue())
								.rsrcDstbCn(alrsrcDstbIns.getRsrcDstbCn())
								.workTypeNm("신규")
								.workerId(worker.getMemberId())
								.build());
			}
		}

		// 자원할당업체 일괄 insert
		cmpnyDao.insertList(insCmpnyList);

		// 자원할당배분 일괄 insert
		dstbDao.insertList(insDstbList);

		// 자원할당배분 최초이력 일괄 insert
		dstbHistDao.insertList(insDstbHistList);

		// 재고 수량 update
		for (invtVsReqDstb invtInf : mvnUtils.getInvtList()) {
			int invtQy = invtInf.getInvtQy() - invtInf.getReqDstbQy();

			fninfDao.updateInvtQy(invtInf.getRsrcId(), invtQy, invtInf.getTotalQy(), worker.getMemberId());
		}
	}

	/**
	 * 자원할당업체 이용기간 변경
	 *
	 * @param param : 이용기간 변경 데이터
	 */
	public void modifyPeriod(AlrsrcCmpnyPeriodParam param)
	{
		Date now = new Date();
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		UsptResrceAsgnEntrps cmpny = cmpnyDao.select(param.getAlrsrcId());
		if (cmpny == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "자원할당업체 정보"));
		}

		if (string.equals(cmpny.getAlrsrcSt(), CodeExt.alrsrcSt.이용종료)) {
			throw new InvalidationException("이용대기이거나 이용중인 상태인 경우에만 변경할 수 있습니다.");
		}

		if ( date.compareDay(string.toDate(cmpny.getAlrsrcEndDay()), string.toDate(param.getAlrsrcEndDay())) == 0 ) {
			throw new InvalidationException("이용기간 종료일이 변경되지 않았습니다.");
		}
		if ( date.compareDay(string.toDate(cmpny.getAlrsrcBgngDay()), string.toDate(param.getAlrsrcEndDay())) > -1 ) {
			throw new InvalidationException(String.format(validateMessageExt.일시_작거나같은비교오류, "이용기간 종료일", "이용기간 시작일", "날짜"));
		}
		if ( date.compareDay(now, string.toDate(param.getAlrsrcEndDay())) > -1 ) {
			throw new InvalidationException(String.format(validateMessageExt.일시_작거나같은비교오류, "이용기간 종료일", "현재일", "날짜"));
		}

		// 이력 작업내용 생성
		StringBuilder workCn = new StringBuilder();
		workCn.append("기존 이용기간 : ").append(cmpny.getFmtAlrsrcBgngDay()).append(" ~ ").append(cmpny.getFmtAlrsrcEndDay()).append("\n");
		workCn.append("변경 이용기간 : ").append(cmpny.getFmtAlrsrcBgngDay()).append(" ~ ").append(param.getFmtAlrsrcEndDay()).append("\n");
		workCn.append(param.getReasonCn());

		// 이력 VO 생성
		UsptResrceAsgnHist hist = UsptResrceAsgnHist.builder()
									.histId(string.getNewId(prefixId.이력ID))
									.histDt(now)
									.alrsrcId(cmpny.getAlrsrcId())
									.workTypeNm("이용기간변경")
									.workCn(workCn.toString())
									.workerId(worker.getMemberId())
									.build();

		// 자원할당업체 이용기간 변경
		cmpny.setAlrsrcEndDay(param.getAlrsrcEndDay());
		cmpny.setReasonCn(param.getReasonCn());
		cmpny.setUpdaterId(worker.getMemberId());
		cmpny.setUpdatedDt(now);

		cmpnyDao.update(cmpny);

		// 이력 등록
		histDao.insert(hist);
	}

	/**
	 * 자원할당상태를 변경
	 *
	 * @param param
	 */
	public void modifyStatus(AlrsrcCmpnyStatusParam param)
	{
		Date now = new Date();
		BnMember worker = SecurityUtils.checkWorkerIsInsider();

		// 입력 데이터 검증
		if (string.isBlank(param.getAlrsrcSt())) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "자원할당상태"));
		}

		if (string.equals(param.getAlrsrcSt(), CodeExt.alrsrcSt.이용종료) && string.isBlank(param.getReasonCn())) {
			throw new InvalidationException(String.format(validateMessageExt.입력없음, "사유"));
		}

		if (!CodeExt.alrsrcSt.isModifyStatus(param.getAlrsrcSt())) {
			throw new InvalidationException(String.format("변경하려는 자원할당상태가 허용하지 않는 코드입니다.[%s]", param.getAlrsrcSt()));
		}

		CodeDto alrsrcStDto = codeDao.select(CodeExt.alrsrcSt.CODE_GROUP, param.getAlrsrcSt());
		if ( alrsrcStDto == null ) {
			throw new InvalidationException(String.format(validateMessageExt.유효오류, "자원할당상태"));
		}

		// 자원할당업체 정보 조회
		UsptResrceAsgnEntrps cmpny = cmpnyDao.select(param.getAlrsrcId());
		if (cmpny == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "자원할당업체 정보"));
		}
		if (string.equals(cmpny.getAlrsrcSt(), CodeExt.alrsrcSt.이용종료)) {
			throw new InvalidationException("현재 '이용종료'인 상태인 경우 자원할당 상태변경을 할 수 없습니다.");
		}

		// 이력관련 변수 선언
		String workTypeNm = "";
		StringBuilder workCn = new StringBuilder();

		// 변경상태별 변경데이터 및 이력데이터 생성
		switch (param.getAlrsrcSt()) {
			case CodeExt.alrsrcSt.이용종료 :
				// 이용기간 종료일을 현재일로 변경
				cmpny.setAlrsrcEndDay(date.format(now, "yyyyMMdd"));

				// 이용종료 이력 생성
				workTypeNm = "이용종료";
				workCn.append(param.getReasonCn());
				break;
			default :
				workTypeNm = "상태변경";
				workCn.append(String.format("‘%s’에서 ‘%s’으로 변경되었습니다.", cmpny.getAlrsrcStNm(), alrsrcStDto.getCodeNm()));
				if (string.isNotBlank(param.getReasonCn())) {
					workCn.append("\n").append(param.getReasonCn());
				}
				break;
		}

		// 이력 VO 생성
		UsptResrceAsgnHist hist = UsptResrceAsgnHist.builder()
									.histId(string.getNewId(prefixId.이력ID))
									.histDt(now)
									.alrsrcId(cmpny.getAlrsrcId())
									.workTypeNm(workTypeNm)
									.workCn(workCn.toString())
									.workerId(worker.getMemberId())
									.build();


		// 자원할당업체 자원할당상태 변경
		cmpny.setAlrsrcSt(param.getAlrsrcSt());
		cmpny.setReasonCn(param.getReasonCn());
		cmpny.setUpdaterId(worker.getMemberId());
		cmpny.setUpdatedDt(now);

		cmpnyDao.update(cmpny);

		// 이용종료인 경우 자원재고수량 변경
		if (string.equals(param.getAlrsrcSt(), CodeExt.alrsrcSt.이용종료)) {
			List<UsptResrceAsgnDstb> uptDstbList = dstbDao.selectList(param.getAlrsrcId());
			if (uptDstbList.isEmpty()) {
				throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "자원업체에 해당하는 자원할당정보"));
			}
			List<UsptResrceInvntryInfo> fninfList = fninfDao.selectList();
			List<UsptResrceInvntryInfo> uptFninfList = new ArrayList<>();
			List<UsptResrceInvntryInfoHist> insFninfHistList = new ArrayList<>();
			List<UsptResrceAsgnDstbHist> insDstbHistList = new ArrayList<>();

			for (UsptResrceAsgnDstb uptDstbInf : uptDstbList) {
				// 자원재고정보 가져오기
				UsptResrceInvntryInfo uptFninf = fninfList.stream().filter(obj->obj.getRsrcId().equals(uptDstbInf.getRsrcId())).findFirst().get();

				// 자원재고정보 재고수량 변경
				int invtQy = uptFninf.getInvtQy() + uptDstbInf.getRsrcDstbQy();
				uptFninf.setInvtQy(invtQy);
				uptFninf.setUpdaterId(worker.getMemberId());
				uptFninf.setUpdatedDt(now);
				uptFninf.setProcTypeCd(CodeExt.procType.수정);

				// 수정대상 목록 추가
				uptFninfList.add(uptFninf);

				// 자원할당정보 변경이력 생성
				UsptResrceAsgnDstbHist insDstbHist = new UsptResrceAsgnDstbHist();
				property.copyProperties(insDstbHist, uptDstbInf);
				insDstbHist.setHistId(string.getNewId(prefixId.이력ID));
				insDstbHist.setHistDt(now);
				insDstbHist.setWorkTypeNm("업체이용종료");
				insDstbHist.setWorkerId(worker.getMemberId());

				insDstbHistList.add(insDstbHist);

				// 자원할당정보 배분수량 변경
				uptDstbInf.setRsrcUseYn(false);
				uptDstbInf.setRsrcDstbQy(0);
				uptDstbInf.setUpdaterId(worker.getMemberId());
				uptDstbInf.setUpdatedDt(now);
			}

			// 자원재고정보 변경이력 생성
			for (UsptResrceInvntryInfo fninf : fninfList) {
				// 수정대상 목록 포함 여부
				boolean isUpdate = uptFninfList.stream().filter(obj->obj.getRsrcId().equals(fninf.getRsrcId())).findFirst().isPresent();

				// 자원재고정보 변경이력 생성
				UsptResrceInvntryInfoHist insFninfHist = new UsptResrceInvntryInfoHist();
				property.copyProperties(insFninfHist, fninf);
				insFninfHist.setHistId(string.getNewId(prefixId.이력ID));
				insFninfHist.setHistDt(now);
				if (isUpdate) {
					insFninfHist.setWorkTypeNm("업체이용종료");
				}
				else {
					insFninfHist.setWorkTypeNm(null);
				}
				insFninfHist.setWorkerId(worker.getMemberId());

				// 이력 생성 목록 추가
				insFninfHistList.add(insFninfHist);
			}

			// 자원할당정보 일괄 수정
			dstbDao.updateList(uptDstbList);

			// 자원재고정보 일괄 수정
			fninfDao.updateList(uptFninfList);

			// 자원할당정보 이력 일괄 등록
			dstbHistDao.insertList(insDstbHistList);

			// 자원재고정보 이력 일괄 등록
			fninfHistDao.insertList(insFninfHistList);
		}

		// 이력 등록
		histDao.insert(hist);
	}

	/**
	 * 자원할당업체 이력 조회
	 *
	 * @param alrsrcId : 자원할당ID
	 * @return 이력 목록
	 */
	public JsonList<UsptResrceAsgnHist> getHist(String alrsrcId)
	{
		SecurityUtils.checkWorkerIsInsider();

		// 자원할당업체 정보 조회
		UsptResrceAsgnEntrps cmpny = cmpnyDao.select(alrsrcId);
		if (cmpny == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "자원할당업체 정보"));
		}

		List<UsptResrceAsgnHist> list = histDao.selectList(alrsrcId);

		return new JsonList<>(list);
	}

	/**
	 * 자원할당사업별 자원할당업체 목록 조회
	 *
	 * @param evlLastSlctnId
	 * @return 자원할당업체 목록 조회
	 */
	public JsonList<AlrsrcCmpnySlctnDto> getBsnsSlctn(String evlLastSlctnId)
	{
		SecurityUtils.checkWorkerIsInsider();

		// 최종선정대상ID를 기준으로 자원할당업체 목록 조회
		List<AlrsrcCmpnySlctnDto> cmpnySlctnList = cmpnyDao.selectList_slctnId( evlLastSlctnId );
		if (cmpnySlctnList.isEmpty()) {
			return new JsonList<>(cmpnySlctnList);
		}

		// 자원할당업체별 최초 자원할당배분정보 조회
		for (AlrsrcCmpnySlctnDto cmpnyInf : cmpnySlctnList) {
			List<AlrsrcFirstDstbDto> alrsrcFirstDstbList = dstbHistDao.selectList_firstDstb( cmpnyInf.getAlrsrcId() );

			cmpnyInf.setFirstDstbList(alrsrcFirstDstbList);
		}

		return new JsonList<>(cmpnySlctnList);
	}
}
