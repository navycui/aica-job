package aicluster.mvn.api.facility.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.security.Code.memberType;
import aicluster.framework.security.Code.validateMessage;
import aicluster.framework.security.SecurityUtils;
import aicluster.mvn.api.facility.dto.MvnFcListParam;
import aicluster.mvn.api.facility.dto.MvnFcParam;
import aicluster.mvn.api.facility.dto.MvnFcRsvtDdParam;
import aicluster.mvn.common.dao.UsptMvnEntrpsInfoDao;
import aicluster.mvn.common.dao.UsptMvnFcltyInfoDao;
import aicluster.mvn.common.dao.UsptMvnFcltyRestdeDao;
import aicluster.mvn.common.dto.MvnFcCodeDto;
import aicluster.mvn.common.dto.MvnFcOfficeRoomDto;
import aicluster.mvn.common.entity.UsptMvnEntrpsInfo;
import aicluster.mvn.common.entity.UsptMvnFcltyInfo;
import aicluster.mvn.common.entity.UsptMvnFcltyRestde;
import aicluster.mvn.common.util.CodeExt.mvnFcType;
import aicluster.mvn.common.util.CodeExt.mvnSt;
import aicluster.mvn.common.util.CodeExt.prefixId;
import aicluster.mvn.common.util.CodeExt.uploadExt;
import aicluster.mvn.common.util.CodeExt.validateMessageExt;
import aicluster.mvn.common.util.MvnUtils;
import bnet.library.exception.ExceptionMessage;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
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
public class MvnFcService {

    @Autowired
    EnvConfig config;
    @Autowired
    private UsptMvnFcltyInfoDao usptMvnFcDao;
    @Autowired
    private UsptMvnFcltyRestdeDao usptMvnFcRsvtDdDao;
    @Autowired
    private AttachmentService atchService;
    @Autowired
    private UsptMvnEntrpsInfoDao usptMvnCompanyDao;
    @Autowired
    private MvnUtils mvnUtils;

    private void validateParam(MvnFcParam param, MultipartFile imageFile)
    {
        // 입력 검증
        InvalidationsException errs = new InvalidationsException();

        // 시설명
        if (string.isBlank(param.getMvnFcNm())) {
            errs.add("mvnFcNm", String.format(validateMessageExt.입력없음, "시설명"));
        }

        // 시설유형
        if (string.isBlank(param.getMvnFcType())) {
            errs.add("mvnFcType", String.format(validateMessageExt.선택없음, "시설유형"));
        }
        else {
            if (!mvnUtils.isValideCode("MVN_FC_TYPE", param.getMvnFcType())) {
                errs.add("mvnFcType", String.format(validateMessageExt.유효오류, "시설유형"));
            }

            // 공유시설인 경우 입력 검증
            if (string.equals(param.getMvnFcType(), mvnFcType.공유시설)) {
                // 시설세부유형
                if (string.isBlank(param.getMvnFcDtype())) {
                    errs.add("mvnFcDtype", String.format(validateMessageExt.선택없음, "시설세부유형"));
                }
                else {
                    if (!mvnUtils.isValideCode("MVN_FC_DTYPE", param.getMvnFcDtype())) {
                        errs.add("mvnFcDtype", String.format(validateMessageExt.유효오류, "시설세부유형"));
                    }
                }
                // 예약유형
                if (string.isBlank(param.getReserveType())) {
                    errs.add("reserveType", String.format(validateMessageExt.선택없음, "예약유형"));
                }
                else {
                    if (!mvnUtils.isValideCode("RESERVE_TYPE", param.getReserveType())) {
                        errs.add("reserveType", String.format(validateMessageExt.유효오류, "예약유형"));
                    }
                }
                // 24시간 이용여부 기본값 지정
            	if (param.getHr24Yn() == null) {
                    param.setHr24Yn(false);
                }

            	// 24시간 이용이 아닌 경우 필수 입력 검증
                if (Boolean.FALSE.equals(param.getHr24Yn())) {
                    // 이용가능시간(시작)
                    if (string.isBlank(param.getUtztnBeginHh())) {
                        errs.add("utztnBeginHh", String.format(validateMessageExt.날짜없음, "이용시작시간"));
                    }
                    // 이용가능시간(종료)
                    if (string.isBlank(param.getUtztnEndHh())) {
                        errs.add("utztnEndHh", String.format(validateMessageExt.날짜없음, "이용종료시간"));
                    }

                    if (string.isNotBlank(param.getUtztnBeginHh()) && string.isNotBlank(param.getUtztnEndHh())) {
	                    String currDay = date.getCurrentDate("yyyyMMdd");
	                    String valideBeginTm = currDay + param.getUtztnBeginHh();
	                    String valideEndTm = currDay + param.getUtztnEndHh();
	                    if (!date.isValidDate(valideBeginTm , "yyyyMMddHH")) {
	                        errs.add("utztnBeginHh", String.format(validateMessageExt.형식오류, "이용시작시간", "00~24"));
	                    }
	                    if (!date.isValidDate(valideEndTm, "yyyyMMddHH")) {
	                        errs.add("utztnEndHh", String.format(validateMessageExt.형식오류, "이용종료시간", "00~24"));
	                    }

	                    if (date.isValidDate(valideBeginTm , "yyyyMMddHH") && date.isValidDate(valideEndTm, "yyyyMMddHH")) {
		                    if (date.truncatedCompareTo(string.toDate(valideBeginTm), string.toDate(valideEndTm), Calendar.HOUR) > -1) {
		                        errs.add("utztnBeginHh", String.format(validateMessageExt.일시_크거나같은비교오류, "이용시작시간", "이용종료시간", "시간"));
		                    }
	                    }
                    }
                }
                // 24시간 이용인 경우 이용시작시간과 이용종료시간 고정값 적용(00, 24)
                else {
                	param.setUtztnBeginHh("00");
                	param.setUtztnEndHh("24");
                }
            }

            // 사무실인 경우 필수 입력 검증
            else if (string.equals(param.getMvnFcType(), mvnFcType.사무실)) {
                if (string.isBlank(param.getBnoCd())) {
                    errs.add("bnoCd", String.format(validateMessageExt.선택없음, "건물동"));
                }
                else {
                    if (!mvnUtils.isValideCode("BNO", param.getBnoCd())) {
                        errs.add("bnoCd", String.format(validateMessage.유효오류, "건물동"));
                    }
                }
                if (string.isBlank(param.getRoomNo())) {
                    errs.add("roomNo", String.format(validateMessage.입력없음, "방호수"));
                }
            }
        }

        // 이미지 파일이 있는 경우 ALT 내용 검증
        if (imageFile != null && string.isBlank(param.getImageAltCn())) {
            errs.add("imageAltCn", String.format(validateMessage.입력없음, "이미지 ALT태그 내용"));
        }

        // 예약불가일 목록이 있는 경우 필수 입력 검증 수행
        if (param.getMvnFcRsvtDdList() != null) {
            List<MvnFcRsvtDdParam> rsvtDdList = param.getMvnFcRsvtDdList();
            int i = 1;
            for (MvnFcRsvtDdParam rsvtDd : rsvtDdList) {
                StringBuilder blankMessage = new StringBuilder();
                blankMessage.append("%d행의 ").append(validateMessage.날짜없음);
                StringBuilder greaterMessage = new StringBuilder();
                greaterMessage.append("%d행의 ").append(validateMessageExt.일시_큰비교오류);

                if (string.isBlank(rsvtDd.getBeginDay())) {
                    errs.add("beginDay", String.format(blankMessage.toString(), i, "시작일"));
                }
                if (string.isBlank(rsvtDd.getEndDay())) {
                    errs.add("endDay", String.format(blankMessage.toString(), i, "종료일"));
                }
                if (string.isNotBlank(rsvtDd.getBeginDay())
                    && string.isNotBlank(rsvtDd.getEndDay())
                    && date.truncatedCompareTo(string.toDate(rsvtDd.getBeginDay()), string.toDate(rsvtDd.getEndDay()), Calendar.DATE) > 0 ) {
	                errs.add("beginDay", String.format(greaterMessage.toString(), i, "시작일["+rsvtDd.getFmtBeginDay()+"]", "종료일["+rsvtDd.getFmtEndDay()+"]", "일자"));
                }
                i++;
            }
        }

        if (errs.size() > 0) {
            log.debug("오류발생 건수 : ["+errs.size()+"]");
            for (ExceptionMessage errMsg : errs.getExceptionMessages()) {
            	log.debug("\t" + errMsg.getMessage());
            }

            throw errs;
        }
    }

    /**
     * 입주시설정보 조회
     *
     * @param mvnFcId : 입주시설ID
     * @return UsptMvnFc
     */
    public UsptMvnFcltyInfo selectInfo(String mvnFcId)
    {
        // 조회
        UsptMvnFcltyInfo mvnFc = usptMvnFcDao.select(mvnFcId);
        List<UsptMvnFcltyRestde> ddList = usptMvnFcRsvtDdDao.selectList( mvnFcId );

        if (mvnFc != null && !ddList.isEmpty()) {
        	mvnFc.setUsptMvnFcRsvtDdList(ddList);
        }

        return mvnFc;
    }

    /**
     * 입주시설 목록조회
     * (업무담당자가 입주시설정보를 목록조회한다.)
     *
     * @param param : 조회조건 (mvnFcNm:입주시설명, mvnFcType:입주시설유형, mvnFcDtype:입주시설상세유형, enabled:사용여부)
     * @param pageParam : 페이징조건
     * @return CorePagination<UsptMvnFc>
     */
    public CorePagination<UsptMvnFcltyInfo> getList(MvnFcListParam param, CorePaginationParam pageParam)
    {
        SecurityUtils.checkWorkerIsInsider();

        // 입력값 유효성 검증
        mvnUtils.validateCode("MVN_FC_TYPE", param.getMvnFcType(), "입주시설 유형");
        mvnUtils.validateCode("MVN_FC_DTYPE", param.getMvnFcDtype(), "입주시설 상세유형");

        // 목록 조회
        long totalItems = usptMvnFcDao.selectList_count( param );
        CorePaginationInfo info = new CorePaginationInfo(pageParam.getPage(), pageParam.getItemsPerPage(), totalItems);
        List<UsptMvnFcltyInfo> list = usptMvnFcDao.selectList( param, info.getBeginRowNum(), info.getItemsPerPage(), totalItems );

        return new CorePagination<>(info, list);
    }

    /**
     * 입주시설 등록
     * (업무담당자가 입주시설 정보를 등록한다.)
     *
     * @param param : 입주시설정보
     * @param imageFile : 입주시설 이미지파일
     * @return UsptMvnFc
     */
    public UsptMvnFcltyInfo add(MvnFcParam param, MultipartFile imageFile)
    {
        BnMember worker = SecurityUtils.checkWorkerIsInsider();

        // 입력값 Validation
        validateParam(param, imageFile);

        // 이미지 파일 업로드
        String imageFileId = null;
        if (imageFile != null && imageFile.getSize() > 0) {
            CmmtAtchmnfl imageAtch = atchService.uploadFile_noGroup(config.getDir().getUpload(), imageFile, uploadExt.imageExt, 0);
            imageFileId = imageAtch.getAttachmentId();
        }

        // 입력 VO 생성
        Date now = new Date();
        String newMvnFcId = string.getNewId(prefixId.시설ID);
        UsptMvnFcltyInfo insMvnFc = UsptMvnFcltyInfo.builder()
                                .imageFileId(imageFileId)
                                .enabled(true)
                                .mvnSt(mvnSt.공실)
                                .mvnStDt(now)
                                .creatorId(worker.getMemberId())
                                .createdDt(now)
                                .updaterId(worker.getMemberId())
                                .updatedDt(now)
                                .build();

        // 입력 VO로 Entity 동일 컬럼 복사
        property.copyProperties(insMvnFc, param);

        // 신규 Key로 매핑
        insMvnFc.setMvnFcId(newMvnFcId);

        usptMvnFcDao.insert(insMvnFc);

        // 예약불가일 생성
        if (param.getMvnFcRsvtDdList() != null) {
            List<UsptMvnFcltyRestde> ddList = new ArrayList<>();
            for (MvnFcRsvtDdParam ddParam : param.getMvnFcRsvtDdList()) {
                UsptMvnFcltyRestde mvnFcRsvtDd = UsptMvnFcltyRestde.builder()
                                                    .mvnFcId(newMvnFcId)
                                                    .beginDay(ddParam.getBeginDay())
                                                    .endDay(ddParam.getEndDay())
                                                    .reason(ddParam.getReason())
                                                    .creatorId(worker.getMemberId())
                                                    .createdDt(now)
                                                    .updaterId(worker.getMemberId())
                                                    .updatedDt(now)
                                                    .build();
                ddList.add(mvnFcRsvtDd);
            }
            usptMvnFcRsvtDdDao.insertList(ddList);
        }

        return selectInfo( newMvnFcId );
    }

    /**
     * 입주시설 상세조회
     * (로그인 사용자들이 입주시설정보를 조회한다.)
     *
     * @param mvnFcId : 입주시설ID
     * @return UsptMvnFc
     */
    public UsptMvnFcltyInfo get(String mvnFcId)
    {
        BnMember worker = SecurityUtils.getCurrentMember();

        UsptMvnFcltyInfo usptMvnFc = selectInfo( mvnFcId );

        if ( worker == null || !string.equals(worker.getMemberType(), memberType.내부사용자) ){
            if ( !string.equals(usptMvnFc.getMvnFcType(), mvnFcType.공유시설) || Boolean.FALSE.equals(usptMvnFc.getEnabled()) ) {
                throw new InvalidationException(String.format(validateMessage.조회결과없음, "시설정보"));
            }
        }

        return usptMvnFc;
    }

    /**
     * 입주시설정보 수정
     * (업무담당자가 입주시설정보를 수정한다.)
     *
     * @param param : 입주시설정보
     * @param imageFile : 입주시설 이미지 파일
     * @return UsptMvnFc
     */
    public UsptMvnFcltyInfo modify(MvnFcParam param, MultipartFile imageFile)
    {
        BnMember worker = SecurityUtils.checkWorkerIsInsider();

        // key 검증
        UsptMvnFcltyInfo mvnFc = usptMvnFcDao.select(param.getMvnFcId());
        if ( mvnFc == null ) {
            throw new InvalidationException(String.format(validateMessage.조회결과없음, "시설정보"));
        }

        // 입력값 Validation
        validateParam(param, imageFile);

        // 이미지 파일 업로드
        String imageFileId = null;
        String oriImageFileId = mvnFc.getImageFileId();
        boolean isChangeImage = false;
        if (imageFile != null && imageFile.getSize() > 0) {
            CmmtAtchmnfl imageAtch = atchService.uploadFile_noGroup(config.getDir().getUpload(), imageFile, uploadExt.imageExt, 0);
            imageFileId = imageAtch.getAttachmentId();
            isChangeImage = true;
        }

        Date now = new Date();
        if ( !string.isEmpty(imageFileId) ) {
        	mvnFc.setImageFileId(imageFileId);
        }
        mvnFc.setUpdaterId(worker.getMemberId());
        mvnFc.setUpdatedDt(now);

        // 입력 VO로 Entity 동일 컬럼 복사
        property.copyProperties(mvnFc, param);

        // 데이터 수정
        usptMvnFcDao.update(mvnFc);

        // 예약불가일 전체 삭제 및 생성
        usptMvnFcRsvtDdDao.delete(mvnFc.getMvnFcId());
        if (param.getMvnFcRsvtDdList() != null) {
            List<UsptMvnFcltyRestde> ddList = new ArrayList<>();
            for (MvnFcRsvtDdParam ddParam : param.getMvnFcRsvtDdList()) {
                UsptMvnFcltyRestde mvnFcRsvtDd = UsptMvnFcltyRestde.builder()
                                                    .mvnFcId(mvnFc.getMvnFcId())
                                                    .beginDay(ddParam.getBeginDay())
                                                    .endDay(ddParam.getEndDay())
                                                    .reason(ddParam.getReason())
                                                    .creatorId(worker.getMemberId())
                                                    .createdDt(now)
                                                    .updaterId(worker.getMemberId())
                                                    .updatedDt(now)
                                                    .build();
                ddList.add(mvnFcRsvtDd);
            }
            usptMvnFcRsvtDdDao.insertList(ddList);
        }

        // 이전 이미지 파일 삭제
        if ( isChangeImage && !string.isEmpty(oriImageFileId) ) {
            atchService.removeFile(config.getDir().getUpload(), oriImageFileId);
        }

        return selectInfo( mvnFc.getMvnFcId() );
    }

    /**
     * 입주시설정보 삭제
     * (업무담당자가 입주시설정보를 삭제한다.)
     *
     * @param mvnFcId : 입주시설ID
     */
    public void delete(String mvnFcId)
    {
        SecurityUtils.checkWorkerIsInsider();

        // key 검증
        UsptMvnFcltyInfo mvnFc = usptMvnFcDao.select(mvnFcId);
        if ( mvnFc == null ) {
            throw new InvalidationException(String.format(validateMessage.조회결과없음, "시설정보"));
        }

        // 입주 배정 점검
        UsptMvnEntrpsInfo mvnCompany = usptMvnCompanyDao.select_mvnFcId(mvnFcId);
        if ( mvnCompany != null ) {
            throw new InvalidationException("해당 입주시설이 배정된 업체가 존재합니다.");
        }

        // 예약불가일 삭제
        usptMvnFcRsvtDdDao.delete(mvnFcId);

        // 입주시설정보 삭제
        usptMvnFcDao.delete(mvnFcId);

        // 입주시설 이미지 삭제
        if ( mvnFc.getImageFileId() != null ) {
            atchService.removeFile(config.getDir().getUpload(), mvnFc.getImageFileId());
        }
    }

    /**
     * 입주시설 이미지파일 삭제
     * (업무담당자가 입주시설의 이미지파일을 삭제한다.)
     *
     * @param mvnFcId : 입주시설ID
     */
    public void deleteImage(String mvnFcId)
    {
        BnMember worker = SecurityUtils.checkWorkerIsInsider();

        // key 검증
        UsptMvnFcltyInfo mvnFc = usptMvnFcDao.select(mvnFcId);
        if ( mvnFc == null ) {
            throw new InvalidationException(String.format(validateMessage.조회결과없음, "시설정보"));
        }

        // 이미지 파일ID 검증
        CmmtAtchmnfl imageFile = atchService.getFileInfo(mvnFc.getImageFileId());
        if ( imageFile == null ) {
            throw new InvalidationException(String.format(validateMessage.조회결과없음, "시설 이미지"));
        }

        // 시설 이미지 관련 데이터 수정
        mvnFc.setImageFileId(null);
        mvnFc.setImageAltCn(null);
        mvnFc.setUpdaterId(worker.getMemberId());
        mvnFc.setUpdatedDt(new Date());

        // 시설정보 갱신
        usptMvnFcDao.update(mvnFc);

        // 이미지 물리파일 삭제
        atchService.removeFile(config.getDir().getUpload(), imageFile.getAttachmentId());
    }

    /**
     * 입주시설 사용여부 전환
     * (업무담당자가 입주시설에 대한 사용여부를 전환한다.)
     *
     * @param mvnFcId : 입주시설ID
     * @param enabled : 사용여부(true/false)
     * @return UsptMvnFc
     */
    public UsptMvnFcltyInfo modifyEnabled(String mvnFcId, Boolean enabled)
    {
        BnMember worker = SecurityUtils.checkWorkerIsInsider();

        // key 검증
        UsptMvnFcltyInfo mvnFc = usptMvnFcDao.select(mvnFcId);
        if ( mvnFc == null ) {
            throw new InvalidationException(String.format(validateMessage.조회결과없음, "시설정보"));
        }

        if ( enabled == null ) {
            throw new InvalidationException(String.format(validateMessage.유효오류, "사용여부"));
        }

        // 입주 배정 점검
        UsptMvnEntrpsInfo mvnCompany = usptMvnCompanyDao.select_mvnFcId(mvnFcId);
        if ( mvnCompany != null ) {
            throw new InvalidationException("해당 입주시설이 배정된 업체가 존재합니다.");
        }

        // 사용여부 데이터 수정
        mvnFc.setEnabled(enabled);
        mvnFc.setUpdaterId(worker.getMemberId());
        mvnFc.setUpdatedDt(new Date());

        // 시설정보 갱신
        usptMvnFcDao.update(mvnFc);

        return selectInfo( mvnFc.getMvnFcId() );
    }

    /**
     * 사용상태인 공유시설 목록조회
     * (모든 사용자들이 사용 상태인 공유시설 유형에 해당하는 입주시설을 목록조회한다.)
     *
     * @param mvnFcDtype : 입주시설상세유형
     * @param pageParam : 페이징조건
     * @return CorePagination<UsptMvnFc>
     */
    public CorePagination<UsptMvnFcltyInfo> getEnableShareList(String mvnFcDtype, CorePaginationParam pageParam)
    {
        // 입력값 유효성 검증
        mvnUtils.validateCode("MVN_FC_DTYPE", mvnFcDtype, "입주시설 상세유형");

        // 목록 조회
        long totalItems = usptMvnFcDao.selectEnableShareList_count( mvnFcDtype );
        CorePaginationInfo info = new CorePaginationInfo(pageParam.getPage(), pageParam.getItemsPerPage(), totalItems);
        List<UsptMvnFcltyInfo> list = usptMvnFcDao.selectEnableShareList( mvnFcDtype, info.getBeginRowNum(), info.getItemsPerPage(), totalItems );

        return new CorePagination<>(info, list);
    }

    /**
     * 사용상태인 사무실 목록조회
     *
     * @param bnoRoomNo : 입주호실
     * @param mvnFcCapacity : 수용인원
     * @param mvnFcar : 시설면적
     * @param pageParam : 페이징조건
     * @return CorePagination<UsptMvnFc>
     */
    public CorePagination<UsptMvnFcltyInfo> getEnableOfficeList(String bnoRoomNo, Long mvnFcCapacity, String mvnFcar, CorePaginationParam pageParam)
    {
        // 입력값 유효성 검증
        mvnUtils.validateBnoRoomNo(bnoRoomNo);

        // 목록 조회
        long totalItems = usptMvnFcDao.selectEnableOfficeList_count( bnoRoomNo, mvnFcCapacity, mvnFcar );
        CorePaginationInfo info = new CorePaginationInfo(pageParam.getPage(), pageParam.getItemsPerPage(), totalItems);
        List<UsptMvnFcltyInfo> list = usptMvnFcDao.selectEnableOfficeList( bnoRoomNo, mvnFcCapacity, mvnFcar, info.getBeginRowNum(), info.getItemsPerPage(), totalItems );

        return new CorePagination<>(info, list);
    }

    /**
     * 입주호실 코드성 목록 조회
     * (사무실로 등록된 입주시설들에 대한 건물동/건물방호수 정보를 기반으로 코드성으로 데이터를 구성하여 조회한다.)
     *
     * @return List<MvnFcOfficeRoomDto>
     */
    public JsonList<MvnFcOfficeRoomDto> getBnoRoomCodeList()
    {
        List<MvnFcOfficeRoomDto> list = usptMvnFcDao.selectBnoRoomCodeList();

        return new JsonList<>(list);
    }

    /**
     * 입주시설 속성값 코드성 목록 조회
     *
     * @param codeType(CAPACITY:수용인원, AREA:시설면적)
     * @return List<MvnFcCodeDto>
     */
    public JsonList<MvnFcCodeDto> getPropCodeList(String codeType)
    {
    	List<MvnFcCodeDto> list = null;
    	switch(codeType.toUpperCase()) {
    		case "CAPACITY" :
    			list = usptMvnFcDao.selectCapacityCodeList();
    			break;
    		case "AREA" :
    		default :
    			list = usptMvnFcDao.selectFcarCodeList();
    			break;
    	}

    	return new JsonList<>(list);
    }
}
