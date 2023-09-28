package aicluster.mvn.api.company.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.dao.FwUserDao;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.UserDto;
import aicluster.framework.common.util.LogIndvdlInfSrchUtils;
import aicluster.framework.common.util.dto.LogIndvdlInfSrch;
import aicluster.framework.notification.EmailUtils;
import aicluster.framework.notification.dto.EmailSendParam;
import aicluster.framework.notification.nhn.email.EmailResult;
import aicluster.framework.security.Code.validateMessage;
import aicluster.framework.security.SecurityUtils;
import aicluster.mvn.api.company.dto.MvnCmpnyAllocateParam;
import aicluster.mvn.api.company.dto.MvnCmpnyCheckoutParam;
import aicluster.mvn.api.company.dto.MvnCmpnyListParam;
import aicluster.mvn.api.company.dto.MvnCmpnyPrcsDto;
import aicluster.mvn.api.request.service.MvnCheckoutService;
import aicluster.mvn.common.dao.UsptMvnChcktHistDao;
import aicluster.mvn.common.dao.UsptMvnChcktReqstDao;
import aicluster.mvn.common.dao.UsptMvnEntrpsPfmcDao;
import aicluster.mvn.common.dao.UsptMvnEntrpsInfoDao;
import aicluster.mvn.common.dao.UsptMvnFcltyInfoDao;
import aicluster.mvn.common.dto.MvnCmpnyAlarmDto;
import aicluster.mvn.common.dto.MvnCmpnyChcktDto;
import aicluster.mvn.common.dto.MvnCmpnyDto;
import aicluster.mvn.common.dto.MvnCmpnyListItemDto;
import aicluster.mvn.common.dto.MvnUserDto;
import aicluster.mvn.common.entity.UsptMvnChcktHist;
import aicluster.mvn.common.entity.UsptMvnChcktReqst;
import aicluster.mvn.common.entity.UsptMvnEntrpsInfo;
import aicluster.mvn.common.entity.UsptMvnFcltyInfo;
import aicluster.mvn.common.util.CodeExt;
import aicluster.mvn.common.util.CodeExt.prefixId;
import aicluster.mvn.common.util.CodeExt.validateMessageExt;
import aicluster.mvn.common.util.MvnUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.property;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.CoreUtils.webutils;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import bnet.library.util.pagination.CorePaginationParam;

@Service
public class MvnCmpnyInfService {

    @Autowired
    private UsptMvnEntrpsInfoDao usptMvnCompanyDao;
    @Autowired
    private UsptMvnFcltyInfoDao usptMvnFcDao;
    @Autowired
    private UsptMvnChcktReqstDao usptCheckoutReqDao;
    @Autowired
    private UsptMvnChcktHistDao usptCheckoutHistDao;
    @Autowired
    private UsptMvnEntrpsPfmcDao usptMvnCmpnyPrfrmDao;
    @Autowired
    private FwUserDao userDao;
    @Autowired
    private EmailUtils emailUtils;
    @Autowired
    private MvnUtils mvnUtils;
    @Autowired
    private MvnCheckoutService checkoutService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private LogIndvdlInfSrchUtils indvdlInfSrchUtils;

    /**
     * 입주업체 목록 조회
     * (업무담당자가 목록 조회한다.)
     *
     * @param param : 조회조건 (mvnCmpnySt:입주업체상태, mvnAllocateSt:입주배정상태, bnoRoomNo:입주호실, enabled:사용여부, cmpnyNm:입주업체명)
     * @param pageParam : 페이징조건
     * @return CorePagination<MvnCmpnyListItemDto>
     */
    public CorePagination<MvnCmpnyListItemDto> getList(MvnCmpnyListParam param, CorePaginationParam pageParam)
    {
        SecurityUtils.checkWorkerIsInsider();

        // 입력값 유효성 검증
        mvnUtils.validateCode("MVN_CMPNY_ST", param.getMvnCmpnySt());
        mvnUtils.validateCode("MVN_ALLOCATE_ST", param.getMvnAllocateSt());
        mvnUtils.validateBnoRoomNo(param.getBnoRoomNo());

        // 목록 조회
        long totalItems = usptMvnCompanyDao.selectList_count( param );
        CorePaginationInfo info = new CorePaginationInfo(pageParam.getPage(), pageParam.getItemsPerPage(), totalItems);
        List<MvnCmpnyListItemDto> list = usptMvnCompanyDao.selectList( param, info.getBeginRowNum(), info.getItemsPerPage(), totalItems );

        return new CorePagination<>(info, list);
    }

    /**
     * 입주업체 상세조회
     * (업무담당자가 입주업체정보를 상세조회한다.)
     *
     * @param mvnId : 입주ID
     * @return MvnCmpnyDto
     */
    public MvnCmpnyDto get(String mvnId)
    {
        BnMember worker = SecurityUtils.checkWorkerIsInsider();

        //1.입주업체 정보를 조회한다.
        UsptMvnEntrpsInfo mvnCompany = usptMvnCompanyDao.select(mvnId);
        if (mvnCompany == null) {
            throw new InvalidationException(String.format(validateMessage.조회결과없음, "입주업체"));
        }

        //2.입주자정보 및 최근 입주성과 제출년월을 조회한다.
        MvnUserDto mvnUser = usptMvnCompanyDao.selectMvnUser(mvnId);
        String recentPresentnYm = usptMvnCmpnyPrfrmDao.selectRecentPresentnYm(mvnId);

        //3.승인된 퇴실신청 정보를 조회한다.
        UsptMvnChcktReqst selChkoutReq = usptCheckoutReqDao.select_mvnId(mvnId);

        //4.응답값 정의
        MvnCmpnyDto cmpnyDto = new MvnCmpnyDto();
        property.copyProperties(cmpnyDto, mvnCompany);

        //4-1.입주업체성과 최근제출년월 및 입주자 정보 세팅
        cmpnyDto.setRecentPresentnYm(recentPresentnYm);
        cmpnyDto.setMvnUser(mvnUser);

        //4-2.입주업체 승인된 퇴실정보 세팅
        if ( selChkoutReq != null && string.equals(selChkoutReq.getCheckoutReqSt(), CodeExt.checkoutReqSt.승인) ) {
        	MvnCmpnyChcktDto chcktDto = new MvnCmpnyChcktDto();
        	property.copyProperties(chcktDto, selChkoutReq);

        	UserDto userDto = userDao.select(selChkoutReq.getUpdaterId());
        	if (userDto != null) {
        		chcktDto.setWorkerId(userDto.getMemberId());
        		chcktDto.setWorkerNm(userDto.getMemberNm());
        	}

        	cmpnyDto.setMvnCheckout(chcktDto);
        }

		// log 정보생성
		if (!string.equals(worker.getMemberId(), mvnUser.getCmpnyId())) {
			LogIndvdlInfSrch logParam = LogIndvdlInfSrch.builder()
										.memberId(worker.getMemberId())
										.memberIp(webutils.getRemoteIp(request))
										.workTypeNm("조회")
										.workCn("입주업체 상세페이지 담당자정보 조회 업무")
										.trgterId(mvnUser.getCmpnyId())
										.email(mvnUser.getChargerEmail())
										.birthday(null)
										.mobileNo(mvnUser.getChargerMobileNo())
										.build();
			indvdlInfSrchUtils.insert(logParam);
		}

        return cmpnyDto;
    }

    /**
     * 입주시설(사무실) 배정
     * (업무담당자가 입주업체에 대해서 입주시설(사무실)을 배정하거나 배정 사무실을 변경 배정한다.)
     *
     * @param param : 입주배정정보 (mvnId:입주ID, mvnFcId:입주시설ID, mvnBeginDay:입주시작일, mvnEndDay:입주종료일, equipPvdtl:장비제공내역)
     */
    public void modifyAllocate(MvnCmpnyAllocateParam param)
    {
        BnMember worker = SecurityUtils.checkWorkerIsInsider();
        Date now = new Date();

        // 필수 입력 검증
        if ( string.isBlank(param.getFmtMvnBeginDay()) ) {
            throw new InvalidationException(String.format(validateMessage.입력없음, "입주시작일"));
        }
        if ( string.isBlank(param.getFmtMnvEndDay()) ) {
            throw new InvalidationException(String.format(validateMessage.입력없음, "입주종료일"));
        }
        if ( date.truncatedCompareTo(string.toDate(param.getFmtMvnBeginDay()), string.toDate(param.getFmtMnvEndDay()), Calendar.DATE) > 0) {
            throw new InvalidationException(String.format(validateMessageExt.일시_큰비교오류, "입주시작일", "입주종료일", "날짜"));
        }

        UsptMvnEntrpsInfo mvnCompany = usptMvnCompanyDao.select(param.getMvnId());
        if (mvnCompany == null) {
            throw new InvalidationException(String.format(validateMessage.조회결과없음, "입주업체 정보"));
        }

        // 동일 회원ID로 입주중인 다른 입주업체 정보가 있는지 검증한다.
        long goingCnt = usptMvnCompanyDao.selectGoing_count(mvnCompany.getCmpnyId(), mvnCompany.getMvnId());
        if (goingCnt > 0) {
            throw new InvalidationException(String.format(validateMessage.조회결과있음, "동일 회원으로 입주중인 별도의 입주업체 정보"));
        }

        // 입력 조건 검증
        if (string.equals(mvnCompany.getMvnCmpnySt(), CodeExt.mvnCmpnySt.입주종료)) {
            throw new InvalidationException(String.format(validateMessageExt.행위상태오류, "대기중 또는 입주중", "입주배정"));
        }

        // 입주시설(사무실) 정보 조회
        UsptMvnFcltyInfo mvnFc = usptMvnFcDao.selectEnable( "OFFICE", param.getMvnFcId() );
        if (mvnFc == null) {
            throw new InvalidationException(String.format(validateMessage.조회결과없음, "입주시설 정보"));
        }

        // 배정되어 있던 입주시설ID 변수 저장
        String bfMvnFcId = mvnCompany.getMvnFcId();

        // 입주업체 정보 DTO 수정
        mvnCompany.setMvnFcId(param.getMvnFcId());
        mvnCompany.setMvnBeginDay(param.getFmtMvnBeginDay());
        mvnCompany.setMvnEndDay(param.getFmtMnvEndDay());
        mvnCompany.setEquipPvdtl(param.getEquipPvdtl());
        mvnCompany.setMvnCmpnySt(CodeExt.mvnCmpnySt.입주중);
        mvnCompany.setMvnCmpnyStDt(now);
        mvnCompany.setMvnAllocateSt(CodeExt.mvnAllocateSt.배정완료);
        mvnCompany.setMvnAllocateStDt(now);
        mvnCompany.setUpdaterId(worker.getMemberId());
        mvnCompany.setUpdatedDt(now);

        // 입주시설 정보 DTO 수정
        mvnFc.setMvnSt(CodeExt.mvnSt.입주);
        mvnFc.setMvnStDt(now);
        mvnFc.setCurOccupantId(mvnCompany.getCmpnyId());
        mvnFc.setUpdaterId(worker.getMemberId());
        mvnFc.setUpdatedDt(now);

        // 입주업체 Update
        usptMvnCompanyDao.update(mvnCompany);

        // 입주시설 Update
        usptMvnFcDao.update(mvnFc);

        // 이전 입주시설ID가 있는 경우 이전 입주시설 정보 수정
        if ( string.isNotBlank(bfMvnFcId) ) {
            UsptMvnFcltyInfo bfMvnFc = usptMvnFcDao.select( bfMvnFcId );

            bfMvnFc.setMvnSt(CodeExt.mvnSt.공실);
            bfMvnFc.setMvnStDt(now);
            bfMvnFc.setCurOccupantId(null);
            bfMvnFc.setUpdaterId(worker.getMemberId());
            bfMvnFc.setUpdatedDt(now);

            usptMvnFcDao.update(bfMvnFc);
        }
    }

    /**
     * 입주업체 퇴실처리
     * (업무담당자가 입주업체에 대해서 퇴실신청을 생성하고 자동 승인처리한다.)
     *
     * @param param : 퇴실신청정보 (mvnId:입주ID, checkoutPlanDay:퇴실예정일, checkoutReason:퇴실사유, equipRtdtl:장비반납내역)
     */
    public void modifyCheckout(MvnCmpnyCheckoutParam param)
    {
        BnMember worker = SecurityUtils.checkWorkerIsInsider();
        Date now = new Date();

        // 필수 입력 검증
        if ( string.isBlank(param.getFmtCheckoutPlanDay()) ) {
            throw new InvalidationException(String.format(validateMessage.입력없음, "퇴실예정일"));
        }
        if ( string.isBlank(param.getCheckoutReason()) ) {
            throw new InvalidationException(String.format(validateMessage.입력없음, "퇴실사유"));
        }

        // 입주업체정보 조회
        UsptMvnEntrpsInfo mvnCompany = usptMvnCompanyDao.select(param.getMvnId());
        if (mvnCompany == null) {
            throw new InvalidationException(String.format(validateMessage.조회결과없음, "입주업체 정보"));
        }
        if (!string.equals(mvnCompany.getMvnCmpnySt(), CodeExt.mvnCmpnySt.입주중)) {
            throw new InvalidationException(String.format(validateMessageExt.행위상태오류, "입주중", "퇴실처리"));
        }

        // 입주시설정보 조회
        UsptMvnFcltyInfo mvnFc = usptMvnFcDao.select( mvnCompany.getMvnFcId() );
        if (mvnFc == null) {
            throw new InvalidationException(String.format(validateMessage.조회결과없음, "입주시설 정보"));
        }

        // 퇴신신청정보 조회
        UsptMvnChcktReqst checkoutReq = usptCheckoutReqDao.select_mvnId( param.getMvnId() );
        if ( checkoutReq == null ) {
            // 퇴실신청 데이터 생성
            checkoutReq = UsptMvnChcktReqst.builder()
	                            .checkoutReqId(string.getNewId(prefixId.퇴실신청ID))
	                            .mvnId(param.getMvnId())
	                            .checkoutReqDt(now)
	                            .checkoutPlanDay(param.getFmtCheckoutPlanDay())
	                            .checkoutReason(param.getCheckoutReason())
	                            .checkoutReqSt(CodeExt.checkoutReqSt.승인)
	                            .checkoutReqStDt(now)
	                            .equipRtdtl(param.getEquipRtdtl())
	                            .mvnCheckoutYn(Boolean.FALSE)
	                            .creatorId(worker.getMemberId())
	                            .createdDt(now)
	                            .updaterId(worker.getMemberId())
	                            .updatedDt(now)
	                            .build();
            usptCheckoutReqDao.insert(checkoutReq);
        }
        else {
        	if (string.equals(checkoutReq.getCheckoutReqSt(), CodeExt.checkoutReqSt.승인)) {
        		StringBuilder message = new StringBuilder(validateMessage.조회결과있음).append("(퇴실예정일:%s)");
        		throw new InvalidationException(String.format(message.toString(), "승인된 퇴실신청 정보", checkoutReq.getFmtCheckoutPlanDay()));
        	}

        	checkoutReq.setCheckoutReqDt(now);
        	checkoutReq.setCheckoutPlanDay(param.getFmtCheckoutPlanDay());
        	checkoutReq.setCheckoutReason(param.getCheckoutReason());
        	checkoutReq.setCheckoutReqSt(CodeExt.checkoutReqSt.승인);
        	checkoutReq.setCheckoutReqStDt(now);
        	checkoutReq.setEquipRtdtl(param.getEquipRtdtl());
        	checkoutReq.setMvnCheckoutYn(Boolean.FALSE);
        	checkoutReq.setUpdaterId(worker.getMemberId());
        	checkoutReq.setUpdatedDt(now);

        	usptCheckoutReqDao.update(checkoutReq);
        }

        // 퇴실신청 이력 데이터 생성
        UsptMvnChcktHist checkoutHist = UsptMvnChcktHist.builder()
	                                        .histId(string.getNewId(prefixId.이력ID))
	                                        .histDt(now)
	                                        .checkoutReqId(checkoutReq.getCheckoutReqId())
	                                        .workTypeNm("퇴실처리")
	                                        .workCn("업무담당자로부터 퇴실등록되었습니다.")
	                                        .workerId(worker.getMemberId())
	                                        .build();
        usptCheckoutHistDao.insert(checkoutHist);

        // 입주업체와 입주시설에 대한 공실 처리를 시도한다.(퇴실예정일이 안되었다면 수행안됨.)
        checkoutReq = usptCheckoutReqDao.select(checkoutReq.getCheckoutReqId());
        checkoutService.updateMvnFcStEmpty(checkoutReq, worker);
    }

    /**
     * 입주사업 최종선정자 동기화
     * (업무담당자가 기준사업의 사업유형이 "입주사업(MVN)"인 사업의 최종선정 회원(입주업체)들에 대한
     * 정보를 입주업체관리로 동기화를 수행한다.)
     *
     * @return MvnCmpnyPrcsDto : 처리건수(동기화 건수)
     */
    public MvnCmpnyPrcsDto add()
    {
        BnMember worker = SecurityUtils.checkWorkerIsInsider();

        // 동기화 대상 목록 조회
        List<UsptMvnEntrpsInfo> syncList = usptMvnCompanyDao.selectSyncTgrtList();

        // 동기화 대상이 있다면 입력 데이터 보완 및 테이블 insert
        if (!syncList.isEmpty()) {
            // 입력 데이터 보완
            Date now = new Date();
            for (UsptMvnEntrpsInfo mvnCmpny : syncList) {
                mvnCmpny.setMvnId(string.getNewId(prefixId.입주ID));
                mvnCmpny.setMvnEtNum(0);
                mvnCmpny.setMvnCmpnySt(CodeExt.mvnCmpnySt.대기중);
                mvnCmpny.setMvnCmpnyStDt(now);
                mvnCmpny.setMvnAllocateSt(CodeExt.mvnAllocateSt.대기중);
                mvnCmpny.setMvnAllocateStDt(now);
                mvnCmpny.setCreatorId(worker.getMemberId());
                mvnCmpny.setCreatedDt(now);
                mvnCmpny.setUpdaterId(worker.getMemberId());
                mvnCmpny.setUpdatedDt(now);
            }

            // 데이터 Insert
            usptMvnCompanyDao.insertList(syncList);
        }

        return new MvnCmpnyPrcsDto(syncList.size());
    }

    /**
     * 입주업체에 배정알림 이메일 발송
     * (업무담당자가 입주시설(사무실) 배정 후 입주업체로 배정알림 이메일을 발송한다.)
     *
     * @param mvnIds : 입주ID 목록
     * @return MvnCmpnyPrcsDto : 처리건수(발송 건수)
     */
    public MvnCmpnyPrcsDto alarm(List<String> mvnIds)
    {
        SecurityUtils.checkWorkerIsInsider();

        // 발송대상 조건문 생성
        StringBuilder whereStr = new StringBuilder();
        for (String mvnId : mvnIds) {
            if ( whereStr.length() > 0 )  whereStr.append(",");

            whereStr.append("'").append(mvnId).append("'");
        }

        if ( whereStr.length() <= 0 ) {
            throw new InvalidationException("알림 발송할 입주업체를 정의하지 않았습니다.");
        }

        // 알림 발송 대상 목록 조회
        List<MvnCmpnyAlarmDto> sendTgrtList = usptMvnCompanyDao.selectSendTgrtList( mvnIds );
        if (sendTgrtList.isEmpty()) {
            throw new InvalidationException("알림 발송할 입주업체 정보가 존재하지 않습니다.");
        }

        // 이메일 제목/내용 세팅
        String emailCn = CoreUtils.file.readFileString("/form/email/email-cmpny-alarm.html");
        if (string.isBlank(emailCn)) {
            throw new InvalidationException("이메일내용 파일을 읽을 수 없습니다.");
        }
        EmailSendParam param = new EmailSendParam();
        param.setContentHtml(true);
        param.setEmailCn(emailCn);
        param.setTitle("[인공지능산업융합사업단] 사무실 입주 배정됨을 알려드립니다.");

        // 수신 대상자 세팅
        for (MvnCmpnyAlarmDto company:sendTgrtList) {
            Map<String, String> templParam = new HashMap<>();
            templParam.put("bnoNm", company.getBnoNm());
            templParam.put("roomNo", company.getRoomNo());
            templParam.put("mvnBeginDay", company.getFmtMvnBeginDay());
            templParam.put("mvnEndDay", company.getFmtMvnEndDay());
            param.addRecipient( company.getEmail(), company.getCmpnyNm(), templParam);
        }

        // 이메일 발송
        EmailResult rs = emailUtils.send(param);
        if (rs.getSuccessCnt() == 0) {
            throw new InvalidationException("이메일 발송에 실패하였습니다. 잠시 후에 다시 이용해 주십시오.");
        }

        // 발송건수 리턴
        return new MvnCmpnyPrcsDto(rs.getSuccessCnt());
    }

}
