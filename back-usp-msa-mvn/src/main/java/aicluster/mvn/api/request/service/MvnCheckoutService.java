package aicluster.mvn.api.request.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.util.LogIndvdlInfSrchUtils;
import aicluster.framework.common.util.dto.LogIndvdlInfSrch;
import aicluster.framework.security.Code;
import aicluster.framework.security.Code.validateMessage;
import aicluster.framework.security.SecurityUtils;
import aicluster.mvn.api.company.dto.MvnCmpnyCheckoutParam;
import aicluster.mvn.api.request.dto.MvnCheckoutListParam;
import aicluster.mvn.api.request.dto.MvnCheckoutStatusParam;
import aicluster.mvn.common.dao.UsptMvnChcktHistDao;
import aicluster.mvn.common.dao.UsptMvnChcktReqstDao;
import aicluster.mvn.common.dao.UsptMvnEntrpsInfoDao;
import aicluster.mvn.common.dao.UsptMvnFcltyInfoDao;
import aicluster.mvn.common.dto.CheckoutReqDto;
import aicluster.mvn.common.dto.CheckoutReqListItemDto;
import aicluster.mvn.common.dto.CheckoutReqStCountDto;
import aicluster.mvn.common.dto.MvnUserDto;
import aicluster.mvn.common.dto.UserMvnCmpnyDto;
import aicluster.mvn.common.entity.UsptMvnChcktHist;
import aicluster.mvn.common.entity.UsptMvnChcktReqst;
import aicluster.mvn.common.entity.UsptMvnEntrpsInfo;
import aicluster.mvn.common.entity.UsptMvnFcltyInfo;
import aicluster.mvn.common.util.CodeExt;
import aicluster.mvn.common.util.CodeExt.checkoutReqSt;
import aicluster.mvn.common.util.CodeExt.mvnCmpnySt;
import aicluster.mvn.common.util.CodeExt.prefixId;
import aicluster.mvn.common.util.CodeExt.validateMessageExt;
import aicluster.mvn.common.util.MvnUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.array;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.property;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.CoreUtils.webutils;
import bnet.library.util.dto.JsonList;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import bnet.library.util.pagination.CorePaginationParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MvnCheckoutService {

    @Autowired
	private UsptMvnChcktReqstDao usptCheckoutReqDao;
    @Autowired
    private UsptMvnChcktHistDao usptCheckoutHistDao;
    @Autowired
    private UsptMvnEntrpsInfoDao usptMvnCompanyDao;
    @Autowired
    private UsptMvnFcltyInfoDao usptMvnFcDao;
    @Autowired
    private MvnUtils mvnUtils;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private LogIndvdlInfSrchUtils indvdlInfSrchUtils;

    /**
     * 퇴실예정일이 도래한 경우 입주업체상태와 입주시설상태를 변경한다.
     * (입주시설에 대한 '공실(Empty)' 처리)
     *
     * @param checkoutReq: 퇴실신청정보
     * @param worker: 작업자정보(Batch에서 호출되는 경우 null로 세팅)
     */
    public void updateMvnFcStEmpty(UsptMvnChcktReqst checkoutReq, BnMember worker)
    {
		// 퇴실예정일이 아직 도래하지 않은 경우, 입주업체와 입주시설 상태 변경은 Batch로 수행한다.
    	Date now = new Date();
    	String workerId;
    	if (worker != null) {
    		workerId = worker.getMemberId();
    	}
    	else {
    		workerId = "BATCH";
    	}

    	// 입주업체퇴실여부가 아직 false인 건 중 퇴실예정일이 현재일자보다 같거나 이전일인 경우 입주업체 및 입주시설을 공실처리한다.
    	if ( string.equals(checkoutReq.getCheckoutReqSt(), CodeExt.checkoutReqSt.승인)
    			&& Boolean.FALSE.equals(checkoutReq.isMvnCheckoutYn())
    			&& date.truncatedCompareTo(string.toDate(checkoutReq.getCheckoutPlanDay()), now, Calendar.DATE) < 1 ) {

    		UsptMvnEntrpsInfo mvnCompany = usptMvnCompanyDao.select(checkoutReq.getMvnId());
    		UsptMvnFcltyInfo mvnFc = usptMvnFcDao.select(mvnCompany.getMvnFcId());

            // 입주업체는 '입주종료' 처리한다.
            mvnCompany.setMvnCmpnySt(CodeExt.mvnCmpnySt.입주종료);
            mvnCompany.setMvnCmpnyStDt(now);
            mvnCompany.setUpdaterId(workerId);
            mvnCompany.setUpdatedDt(now);
            usptMvnCompanyDao.update(mvnCompany);

            // 입주시설은 '공실' 처리한다.
            mvnFc.setMvnSt(CodeExt.mvnSt.공실);
            mvnFc.setMvnStDt(now);
            mvnFc.setCurOccupantId(null);
            mvnFc.setUpdaterId(workerId);
            mvnFc.setUpdatedDt(now);
            usptMvnFcDao.update(mvnFc);

            // 퇴실신청 테이블의 입주업체퇴실여부 true 처리
            checkoutReq.setMvnCheckoutYn(Boolean.TRUE);
            usptCheckoutReqDao.update(checkoutReq);

            // 퇴실처리 이력을 남긴다.
        	UsptMvnChcktHist hist = UsptMvnChcktHist.builder()
											.histId(string.getNewId(prefixId.이력ID))
											.histDt(now)
											.checkoutReqId(checkoutReq.getCheckoutReqId())
											.workTypeNm("퇴실처리")
											.workCn("입주업체와 입주시설에 대해서 퇴실처리하였습니다.")
											.workerId(workerId)
											.build();
        	usptCheckoutHistDao.insert(hist);
    	}
    }

    /**
     * 퇴실신청 등록
     * (입주업체의 업체담당자가 퇴실신청을 수행한다.)
     *
     * @param param : 퇴실신청정보 (checkoutPlanDay:퇴실예정일, checkoutReason:퇴실사유)
     * @return UsptCheckoutReq
     */
    public UsptMvnChcktReqst add(MvnCmpnyCheckoutParam param)
    {
        BnMember worker = SecurityUtils.checkWorkerIsMember();

        // 입주업체 조회(입주중 상태인 최근 입주업체 정보를 조회한다.)
        UserMvnCmpnyDto mvnCmpny = usptMvnCompanyDao.selectUser(worker.getMemberId(), true);
        if (mvnCmpny == null) {
            throw new InvalidationException(String.format(validateMessage.조회결과없음, "입주중인 입주업체 정보"));
        }

        // 퇴실신청 정보 조회
        UsptMvnChcktReqst selChkoutReq = usptCheckoutReqDao.select_mvnId(mvnCmpny.getMvnId());
        if (selChkoutReq != null) {
            throw new InvalidationException(String.format(validateMessage.조회결과있음, "퇴실신청 정보"));
        }

        // 필수입력 검증
        if (string.isBlank(param.getFmtCheckoutPlanDay())) {
            throw new InvalidationException(String.format(validateMessage.입력없음, "퇴실예정일"));
        }
        if (string.isBlank(param.getCheckoutReason())) {
            throw new InvalidationException(String.format(validateMessage.입력없음, "퇴실사유"));
        }

        // 입력 권한 및 조건 검증
        if (!string.equals(mvnCmpny.getMvnCmpnySt(), mvnCmpnySt.입주중)) {
            throw new InvalidationException(String.format(validateMessageExt.행위상태오류, "입주중", "퇴실신청"));
        }

        // 입주시설 정보 조회
        UsptMvnFcltyInfo mvnFc = usptMvnFcDao.select(mvnCmpny.getMvnFcId());
        if (mvnFc == null) {
            throw new InvalidationException(String.format(validateMessage.조회결과없음, "입주시설 정보"));
        }

        // 입력 데이터 생성
        Date now = new Date();
        String checkoutReqId = string.getNewId("ckout-");
        UsptMvnChcktReqst checkoutReq = UsptMvnChcktReqst.builder()
                                                    .checkoutReqId(checkoutReqId)
                                                    .mvnId(mvnCmpny.getMvnId())
                                                    .checkoutReqDt(now)
                                                    .checkoutPlanDay(param.getFmtCheckoutPlanDay())
                                                    .checkoutReason(param.getCheckoutReason())
                                                    .checkoutReqSt(checkoutReqSt.신청)
                                                    .checkoutReqStDt(now)
                                                    .mvnCheckoutYn(Boolean.FALSE)
                                                    .creatorId(worker.getMemberId())
                                                    .createdDt(now)
                                                    .updaterId(worker.getMemberId())
                                                    .updatedDt(now)
                                                    .build();
        usptCheckoutReqDao.insert(checkoutReq);

        // 이력 생성
        UsptMvnChcktHist checkoutHist = UsptMvnChcktHist.builder()
                                                    .histId(string.getNewId(prefixId.이력ID))
                                                    .histDt(now)
                                                    .checkoutReqId(checkoutReqId)
                                                    .workTypeNm("신청")
                                                    .workCn("신청 처리되었습니다.")
                                                    .workerId(worker.getMemberId())
                                                    .build();
        usptCheckoutHistDao.insert(checkoutHist);

        return usptCheckoutReqDao.select(checkoutReqId);
    }

    /**
     * 퇴실신청 목록조회
     * (업무담당자가 퇴실신청상태가 '신청취소'가 아닌 건들만 대상으로 퇴실신청 목록을 조회한다.)
     *
     * @param checkoutReqSt
     * @param bnoRoomNo
     * @param memberNm
     * @param pageParam
     * @return
     */
    public CorePagination<CheckoutReqListItemDto> getList( MvnCheckoutListParam param, CorePaginationParam pageParam )
    {
        SecurityUtils.checkWorkerIsInsider();

        mvnUtils.validateCode("CHECKOUT_REQ_ST", param.getCheckoutReqSt(), "퇴신신청상태");
        mvnUtils.validateBnoRoomNo(param.getBnoRoomNo());

        if ( string.equals(param.getCheckoutReqSt(), CodeExt.checkoutReqSt.신청취소) ) {
        	throw new InvalidationException(validateMessageExt.재선택권유);
        }

        long totalItems = usptCheckoutReqDao.selectList_count( param );

        CorePaginationInfo info = new CorePaginationInfo(pageParam.getPage(), pageParam.getItemsPerPage(), totalItems);
        List<CheckoutReqListItemDto> list = usptCheckoutReqDao.selectList( param, info.getBeginRowNum(), info.getItemsPerPage(), totalItems);

        return new CorePagination<>(info, list);
    }

    public CheckoutReqDto get(String checkoutReqId)
    {
        BnMember worker = SecurityUtils.checkWorkerIsInsider();

        UsptMvnChcktReqst checkoutReq = usptCheckoutReqDao.select(checkoutReqId);
        if (checkoutReq == null) {
            throw new InvalidationException(String.format(validateMessage.조회결과없음, "퇴실신청 정보"));
        }

        UsptMvnEntrpsInfo mvnCompany = usptMvnCompanyDao.select(checkoutReq.getMvnId());
        UsptMvnFcltyInfo mvnFc = usptMvnFcDao.select(mvnCompany.getMvnFcId());
        MvnUserDto mvnUser = usptMvnCompanyDao.selectMvnUser(checkoutReq.getMvnId());

        CheckoutReqDto output = new CheckoutReqDto();
        property.copyProperties(output, checkoutReq);

        output.setMvnCompany(mvnCompany);
        output.setMvnFc(mvnFc);
        output.setMvnUser(mvnUser);

		// log 정보생성
		if (!string.equals(worker.getMemberId(), mvnUser.getCmpnyId())) {
			LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
										.memberId(worker.getMemberId())
										.memberIp(webutils.getRemoteIp(request))
										.workTypeNm("조회")
										.workCn("퇴실신청 상세페이지 담당자정보 조회 업무")
										.trgterId(mvnUser.getCmpnyId())
										.email(mvnUser.getChargerEmail())
										.birthday(null)
										.mobileNo(mvnUser.getChargerMobileNo())
										.build();
			indvdlInfSrchUtils.insert(logParam);
		}

        return output;
    }

    public void modifyStatus(MvnCheckoutStatusParam param)
    {
        BnMember worker = SecurityUtils.checkLogin();

        // 필수 입력값 검증
        UsptMvnChcktReqst checkoutReq = usptCheckoutReqDao.select(param.getCheckoutReqId());
        if (checkoutReq == null) {
            throw new InvalidationException(String.format(validateMessage.조회결과없음, "퇴실신청 정보"));
        }

        if (string.isBlank(param.getCheckoutReqSt())) {
            throw new InvalidationException(String.format(validateMessageExt.선택없음, "퇴실신청상태"));
        }
        else {
            mvnUtils.validateCode("CHECKOUT_REQ_ST", param.getCheckoutReqSt(), "퇴실신청상태");
        }

        // 입주업체 정보 조회
        UsptMvnEntrpsInfo mvnCompany = usptMvnCompanyDao.select(checkoutReq.getMvnId());
        if (mvnCompany == null) {
            throw new InvalidationException(String.format(validateMessage.조회결과없음, "입주업체 정보"));
        }

        // 입주시설 정보 조회
        UsptMvnFcltyInfo mvnFc = usptMvnFcDao.select(mvnCompany.getMvnFcId());
        if (mvnFc == null) {
            throw new InvalidationException(String.format(validateMessage.조회결과없음, "입주시설 정보"));
        }

        // 퇴신신청상태별 조건 검증을 한다.
        switch(param.getCheckoutReqSt()) {
            case CodeExt.checkoutReqSt.승인 :
            case CodeExt.checkoutReqSt.보완 :
                if(!Code.memberType.isInsider(worker.getMemberType())) {
                    throw new InvalidationException(validateMessageExt.권한없음);
                }
                if (string.equals(param.getCheckoutReqSt(), CodeExt.checkoutReqSt.보완) && string.isBlank(param.getMakeupReqCn())) {
                    throw new InvalidationException(String.format(validateMessage.입력없음, "보완요청내용"));
                }
                break;

            case CodeExt.checkoutReqSt.신청취소:
                if (!string.equals(mvnCompany.getCmpnyId(), worker.getMemberId())) {
                    throw new InvalidationException(String.format(validateMessageExt.행위오류, "업체담당자", "신청취소"));
                }
                if (!string.equals(checkoutReq.getCheckoutReqSt(), CodeExt.checkoutReqSt.신청)) {
                    throw new InvalidationException(String.format(validateMessageExt.행위상태오류, "신청", "신청취소"));
                }
                break;

            default :
                throw new InvalidationException(String.format(validateMessage.유효오류, "퇴실신청상태"));
        }

        // 퇴실신청상태 정보를 갱신한다.
        Date now = new Date();
        checkoutReq.setCheckoutReqSt(param.getCheckoutReqSt());
        checkoutReq.setCheckoutReqStDt(now);
        if (string.equals(param.getCheckoutReqSt(), CodeExt.checkoutReqSt.승인)) {
            checkoutReq.setEquipRtdtl(param.getEquipRtdtl());
        }
        else if (string.equals(param.getCheckoutReqSt(), CodeExt.checkoutReqSt.보완)) {
            checkoutReq.setMakeupReqCn(param.getMakeupReqCn());
        }
        checkoutReq.setUpdaterId(worker.getMemberId());
        checkoutReq.setUpdatedDt(now);
        usptCheckoutReqDao.update(checkoutReq);

        // 퇴실신청변경이력을 등록한다.
        UsptMvnChcktHist checkoutHist = UsptMvnChcktHist.builder()
                                            .histId(string.getNewId(prefixId.이력ID))
                                            .histDt(now)
                                            .checkoutReqId(param.getCheckoutReqId())
                                            .workerId(worker.getMemberId())
                                            .build();
        if (string.equals(param.getCheckoutReqSt(), CodeExt.checkoutReqSt.승인)) {
            checkoutHist.setWorkTypeNm("승인");
            checkoutHist.setWorkCn("승인 처리되었습니다.");
        }
        else if (string.equals(param.getCheckoutReqSt(), CodeExt.checkoutReqSt.보완)) {
            checkoutHist.setWorkTypeNm("보완");
            checkoutHist.setWorkCn("보완요청 처리되었습니다.");
        }
        else if (string.equals(param.getCheckoutReqSt(), CodeExt.checkoutReqSt.신청취소)) {
            checkoutHist.setWorkTypeNm("신청취소");
            checkoutHist.setWorkCn("신청취소 처리되었습니다.");
        }
        usptCheckoutHistDao.insert(checkoutHist);

        // 승인 처리인 경우 입주업체 상태와 입주시설 상태를 변경을 시도한다.(퇴실예정일이 안되었다면 수행안됨.)
        if (string.equals(param.getCheckoutReqSt(), CodeExt.checkoutReqSt.승인)) {
	        checkoutReq = usptCheckoutReqDao.select(checkoutReq.getCheckoutReqId());
	        updateMvnFcStEmpty(checkoutReq, worker);
        }
    }

    public JsonList<UsptMvnChcktHist> getHist(String checkoutReqId)
    {
        SecurityUtils.checkWorkerIsInsider();

        List<UsptMvnChcktHist> list = usptCheckoutHistDao.selectList(checkoutReqId);

        return new JsonList<>(list);
    }

    public UsptMvnChcktReqst getUser(String mvnId)
    {
        BnMember worker = SecurityUtils.checkLogin();

        return usptCheckoutReqDao.select_cmpnyId(mvnId, worker.getMemberId());
    }

    public UsptMvnChcktReqst modify(MvnCmpnyCheckoutParam param)
    {
        BnMember worker = SecurityUtils.checkLogin();

        // 필수입력 검증
        log.debug(param.toString());
        UsptMvnChcktReqst checkoutReq = usptCheckoutReqDao.select(param.getCheckoutReqId());
        if (checkoutReq == null) {
            throw new InvalidationException(String.format(validateMessage.조회결과없음, "퇴실신청 정보"));
        }
        if (string.isBlank(param.getFmtCheckoutPlanDay())) {
            throw new InvalidationException(String.format(validateMessage.입력없음, "퇴실예정일"));
        }
        if (string.isBlank(param.getCheckoutReason())) {
            throw new InvalidationException(String.format(validateMessage.입력없음, "퇴실사유"));
        }

        // 수정 권한/조건 검증
        UsptMvnEntrpsInfo mvnCompany = usptMvnCompanyDao.select(checkoutReq.getMvnId());
        if (mvnCompany == null) {
            throw new InvalidationException(String.format(validateMessage.조회결과없음, "입주업체 정보"));
        }
        if (!string.equals(mvnCompany.getCmpnyId(), worker.getMemberId())) {
            throw new InvalidationException(String.format(validateMessageExt.행위오류, "입주업체 담당자", "수정"));
        }
        String[] arrCheckoutReqSt = {CodeExt.checkoutReqSt.신청취소, CodeExt.checkoutReqSt.보완};
        if (!array.contains(arrCheckoutReqSt, checkoutReq.getCheckoutReqSt())) {
            throw new InvalidationException(String.format(validateMessageExt.행위상태오류, "신청취소 또는 보완", "수정"));
        }

        // 수정 데이터 적용(이전 상태가 '신청취소'인 경우 퇴실신청일을 현재일자로 수정한다.)
        Date now = new Date();
        if (string.equals(checkoutReq.getCheckoutReqSt(), CodeExt.checkoutReqSt.신청취소)) {
        	checkoutReq.setCheckoutReqDt(now);
        }
        checkoutReq.setCheckoutPlanDay(param.getFmtCheckoutPlanDay());
        checkoutReq.setCheckoutReason(param.getCheckoutReason());
        checkoutReq.setCheckoutReqSt(CodeExt.checkoutReqSt.신청);
        checkoutReq.setCheckoutReqStDt(now);
        checkoutReq.setUpdaterId(worker.getMemberId());
        checkoutReq.setUpdatedDt(now);
        usptCheckoutReqDao.update(checkoutReq);

        // 이력 데이터 생성
        UsptMvnChcktHist hist = UsptMvnChcktHist.builder()
                                    .histId(string.getNewId(prefixId.이력ID))
                                    .histDt(now)
                                    .checkoutReqId(checkoutReq.getCheckoutReqId())
                                    .workTypeNm("수정신청")
                                    .workCn("수정신청 처리되었습니다.")
                                    .workerId(worker.getMemberId())
                                    .build();
        usptCheckoutHistDao.insert(hist);

        return usptCheckoutReqDao.select(checkoutReq.getCheckoutReqId());
    }

    /**
     * 퇴실신청상태별 건수 조회
     *
     * @return
     */
	public CheckoutReqStCountDto getStatusCount()
	{
		// 내부사용자 로그인 검증
		SecurityUtils.checkWorkerIsInsider();

		// 퇴실신청상태별 건수 조회
		return usptCheckoutReqDao.selectChekcoutReqSt_count();
	}
}
