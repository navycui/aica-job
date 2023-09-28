package aicluster.tsp.api.common;

import aicluster.framework.common.dao.FwUserDao;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.framework.common.entity.CmmtAtchmnflGroup;
import aicluster.framework.common.entity.UserDto;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.notification.EmailUtils;
import aicluster.framework.notification.SmsUtils;
import aicluster.framework.notification.dto.EmailSendParam;
import aicluster.framework.notification.dto.SmsSendParam;
import aicluster.framework.notification.nhn.sms.SmsRecipient;
import aicluster.tsp.api.common.param.*;
import aicluster.tsp.common.dao.CmmtCodeDao;
import aicluster.tsp.common.dto.ApplcntDto;
import aicluster.tsp.common.dto.EqpmnRntfeeDto;
import aicluster.tsp.common.util.TspCode;
import aicluster.tsp.common.util.TspUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
@Slf4j
@Service
public class CommonService {

    @Autowired
    private CmmtCodeDao cmmtCodeDao;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private EnvConfig config;

    @Autowired
    private FwUserDao fwUserDao;

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private SmsUtils smsUtils;

    // 확장자
//    "jpg", "jpeg","png", "gif", "pdf", "gif", "xlsx", "pptx", "xlsx", "docx", "doc", "hwp"
    String[] allowedFileExts = {"png", "pdf", "hwp"};

    public CommonCodeParam getcodelist(List<String> list) {
        HashMap<String, CommonCodeGroup> codemap = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            CommonCodeGroup codeGroup = new CommonCodeGroup();
            codeGroup.setCodeGroup(cmmtCodeDao.selectCodeNameList(list.get(i)));
            codemap.put(list.get(i).toUpperCase(), codeGroup);
        }
        CommonCodeParam common = new CommonCodeParam();
        common.setCodeGroup(codemap);

//        HashMap<HashMap<String, Integer>> test = new ArrayList<>();
//        test = cmmtCodeDao.selectStatistics("2022-01-01", "2023-01-01");

        return common;
    }

//  public CommonCodeParam getcodelist(List<String> list) {
//    HashMap<String, List<EqpmnCodeDto>> codemap = new HashMap<>();
//    for (int i = 0; i < list.size(); i++) {
//      List<EqpmnCodeDto> codelist = cmmtCodeDao.selectCodeNameList(list.get(i));
//      codemap.put(list.get(i).toUpperCase(), codelist);
//    }
//
//    CommonCodeParam common = new CommonCodeParam();
//    common.setCodeGroup(codemap);
//
//    return common;
//  }

    public CommonCalcParam getUseFulHour(Integer usefulBeginHour, Integer usefulEndHour) {
        Integer startLunchTime = 12;
        Integer endLunchTime = 13;
        Integer usefulHour = startLunchTime - usefulBeginHour + usefulEndHour - endLunchTime;   // 1일 가용 시간
        if (usefulBeginHour >= endLunchTime || usefulEndHour <= startLunchTime) {
            usefulHour = usefulEndHour - usefulBeginHour;   // 1일 가용 시간
        }
        CommonCalcParam calc  = CommonCalcParam.builder()
                .usefulHour(usefulHour)
                .build();
        return calc;
    }

    /**
     * 장비 사용료
     */
    public CommonCalcParam getWorkingRntfee(String eqpmnId, Date startDt, Date endDt, Boolean tkout) {

        DateFormat df = new SimpleDateFormat("yyyyMMdd");

        List<List<Object>> testt = cmmtCodeDao.selectWorkingRntfee(eqpmnId, df.format(startDt), df.format(endDt));

        EqpmnRntfeeDto eqpmnRntfeeDto = (EqpmnRntfeeDto) testt.get(0).get(0);
        List holidayList = (ArrayList) testt.get(1);

        String setDay = df.format(startDt.getTime());

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(startDt);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(endDt);
        int workingdays = 0;
        int notWorkingdays = 0;
        int holidays = 0;

        while (cal1.before(cal2) || cal1.equals(cal2) ) {
            // 반출시 휴일 포함 || 미반출시 휴일 포함
            // 법정 공휴일 휴일 포함
            if ((Calendar.SATURDAY != cal1.get(Calendar.DAY_OF_WEEK) && Calendar.SUNDAY != cal1.get(Calendar.DAY_OF_WEEK) && !holidayList.contains(setDay))
            ) {
                workingdays++;
            } else if (!holidayList.contains(setDay)) {
                notWorkingdays++;
            } else {
                holidays++;
            }
            cal1.add(Calendar.DATE, 1);
            setDay = df.format(cal1.getTime());
        }
        if ((tkout && eqpmnRntfeeDto.getTkoutHldyInclsAt() || (!tkout && eqpmnRntfeeDto.getNttkoutHldyInclsAt()))) {
            workingdays = workingdays + notWorkingdays;
        }
        if (eqpmnRntfeeDto.getHldyInclsAt()) {
            workingdays = workingdays + holidays;
        }
        Integer usefulBeginHour = eqpmnRntfeeDto.getUsefulBeginHour();  // 1일 가용 시작시간
        Integer usefulEndHour = eqpmnRntfeeDto.getUsefulEndHour();  // 1일 가용 종료 시간
        Integer startLunchTime = 12;
        Integer endLunchTime = 13;
        Integer usefulHour = startLunchTime - usefulBeginHour + usefulEndHour - endLunchTime;   // 1일 가용 시간
        if (usefulBeginHour > endLunchTime || usefulEndHour < startLunchTime){
            usefulHour = usefulEndHour - usefulBeginHour;   // 1일 가용 시간
        }

        Integer startHrs = cal1.get(Calendar.HOUR_OF_DAY) ;
        Integer endHrs = cal2.get(Calendar.HOUR_OF_DAY);
        Integer startMin = cal1.get(Calendar.MINUTE) ;
        Integer endMin = cal2.get(Calendar.MINUTE);
        if ( startHrs < usefulBeginHour) {
            startHrs = usefulBeginHour;
            startMin = 0;
        }
        if ( endHrs > usefulEndHour) {
            endHrs = usefulEndHour;
            endMin = 0;
        }

        Integer startDayUseFullHrs = (usefulEndHour - endLunchTime + startLunchTime - startHrs) + (endHrs - endLunchTime + startLunchTime - usefulBeginHour);
        if (startDayUseFullHrs > Integer.MAX_VALUE) {
            throw new InvalidationException(String.format(TspCode.validateMessage.유효오류, "데이터"));
        }
        if (startHrs > endLunchTime || endHrs < startLunchTime){
            startDayUseFullHrs = (usefulEndHour - startHrs) + (endHrs - usefulBeginHour);
            if (startDayUseFullHrs > Integer.MAX_VALUE) {
                throw new InvalidationException(String.format(TspCode.validateMessage.유효오류, "데이터"));
            }
        }
        Integer startDayUseFullMin = endMin - startMin;

        Integer addHour = startDayUseFullMin < -30 ? -1 : (startDayUseFullMin < 30 ? 0 : 1);

        // 시간 계산을 위해 앞뒤 날짜 자름
//        workingdays = workingdays > 1 ? workingdays - 2 : 0;
        Integer TotalHrs = ((workingdays- 2) * usefulHour) + startDayUseFullHrs + addHour;

        Integer totalmin = (((workingdays - 2) * usefulHour) + startDayUseFullHrs -1) * 60 + endMin + 60 - startMin;
        // 사용료
        Integer rntfee = TotalHrs * eqpmnRntfeeDto.getRntfeeHour();

        if (tkout) {
            totalmin = Long.valueOf(Math.round((endDt.getTime() - startDt.getTime()) / 1000.0 / 60.0)).intValue();
            TotalHrs = Long.valueOf(Math.round((endDt.getTime() - startDt.getTime()) / 1000.0 / 60.0 / 60.0)).intValue();
            if (!eqpmnRntfeeDto.getTkoutHldyInclsAt()) {
                totalmin = totalmin - (24*60) * notWorkingdays;
                TotalHrs = TotalHrs - (24) * notWorkingdays;
            }
            rntfee = TotalHrs * eqpmnRntfeeDto.getRntfeeHour();
        }
        CommonCalcParam calc = CommonCalcParam.builder()
                .rntfeeHour(eqpmnRntfeeDto.getRntfeeHour())
                .usefulBeginHour(eqpmnRntfeeDto.getUsefulBeginHour())
                .usefulEndHour(eqpmnRntfeeDto.getUsefulEndHour())
                .totalHrs(TotalHrs)
                .rntfee(rntfee)
                .totalMin(totalmin)
                .usefulHour(usefulHour)
                .build();

        return calc;
    }
    public CommonReturnList eqpmnClList(){

        CommonReturnList data = new CommonReturnList(cmmtCodeDao.selectEqpmnClNm());

        return data;
    }

    public ApplcntDto getApplcnt(){
        BnMember worker = TspUtils.getMember();

        UserDto user = fwUserDao.select(worker.getMemberId());
        ApplcntDto applct = cmmtCodeDao.selectApplcnt(worker.getMemberId());

        if(applct == null)
        {
            // 신청자 정보 없을경우
            applct = new ApplcntDto();
        }
        else{
            if(CoreUtils.string.isNotBlank(applct.getBsnlcnsFileId())){
                applct.setBsnlcnsFile(getAttachmentGroup(applct.getBsnlcnsFileId()).get(0));
            }
        }

        applct.setMberDiv(worker.getMemberType());
        applct.setEntrprsNm(worker.getMemberNm());
        applct.setUserNm(worker.getMemberNm());

        applct.setEmail(user.getEmail());
        applct.setCttpc(user.getMobileNo());
        return applct;
    }

    public ApplcntDto getApplcnt(String mberId){
        UserDto user = fwUserDao.select(mberId);
        ApplcntDto applct = cmmtCodeDao.selectApplcnt(mberId);

        if(applct == null)
        {
            // 신청자 정보 없을경우
            applct = new ApplcntDto();
        }
        else{
            if(CoreUtils.string.isNotBlank(applct.getBsnlcnsFileId())){
                applct.setBsnlcnsFile(getAttachmentGroup(applct.getBsnlcnsFileId()).get(0));
            }
        }
        applct.setMberDiv(user.getMemberType());
        applct.setEntrprsNm(user.getMemberNm());
        applct.setUserNm(user.getMemberNm());
        applct.setEmail(user.getEmail());
        applct.setCttpc(user.getMobileNo());
        return applct;
    }

    public void putApplcnt(CommonApplcntParam param){
        BnMember worker = TspUtils.getMember();

        param.setApplcntId(worker.getMemberId());
        cmmtCodeDao.updateApplcnt(param);
    }

    public void postApplcnt(CommonApplcntParam param){
        BnMember worker = TspUtils.getMember();

        param.setApplcntId(worker.getMemberId());
        cmmtCodeDao.insertApplcnt(param);
    }

    public List<String> getYmd(){
        List<String> holidayList = cmmtCodeDao.getYmd();
        return holidayList;
    }

    public List<CommonAttachmentParam> getAttachmentGroup(String atchmnflGroupId){
        List<CommonAttachmentParam> dto = cmmtCodeDao.selectAttachmentGroup(atchmnflGroupId);
        return dto;
    }

    public CommonAttachmentParam getAttachment(String atchmnflId){
        CommonAttachmentParam dto = cmmtCodeDao.selectAttachment(atchmnflId);
        return dto;
    }

    public CommonAttachmentParam getAttachmentInfoGroup(String atchmnflGroupId){
        Long Data = cmmtCodeDao.selectAttachmentInfoGroupCount(atchmnflGroupId);
        if (Data > 1){
            throw new InvalidationException(String.format(TspCode.validateMessage.DB오류, "데이터"));
        }
        CommonAttachmentParam dto = cmmtCodeDao.selectAttachmentInfoGroup(atchmnflGroupId);
        return dto;
    }

    public void putEqpmnUseSttus(CommonEqpmnUseSttusParam param){
        BnMember worker = TspUtils.getMember();

        param.setUpdusrId(worker.getMemberId());
        cmmtCodeDao.updateEqpmnUseSttus(param);
    }

    // 이메일 발송
    public int sendEmail(String title, String emailCn, ApplcntDto applcnt, Map<String, String> templateParam) {

        EmailSendParam param = new EmailSendParam();
        param.setContentHtml(true);
        param.setTitle(title);
        param.setEmailCn(emailCn);

        List<String> emailList = new ArrayList<>();

        emailList.add(applcnt.getEmail());
        param.addRecipient(applcnt.getEmail(), applcnt.getUserNm(), templateParam);

        int sendCnt = (param.getRecipientList() == null) ? 0 : param.getRecipientList().size();
        if (sendCnt > 0) {
            emailUtils.send(param);
        }
        return sendCnt;
    }

    // sms 문자 발송
    public int sendSms(String smsCn, ApplcntDto applcnt, Map<String, String> templateParam) {

        SmsSendParam param = new SmsSendParam();
        List<String> mobileNoList = new ArrayList<>();
        mobileNoList.add(applcnt.getCttpc());
        param.setMobileNoList(mobileNoList);
        param.setSmsCn(smsCn);

        SmsRecipient recipient = new SmsRecipient();
        recipient.setRecipientNo(applcnt.getCttpc());
        recipient.setTemplateParameter(templateParam);
        param.setRecipientList(Arrays.asList(recipient));

        int sendCnt = (param.getRecipientList() == null) ? 0 : param.getRecipientList().size();
        if (sendCnt > 0) {
            // 발송 필요시 주석 제거
            //smsUtils.send(param);
        }
        return sendCnt;
    }

    /**
     * attachmentId 경로
     */
//    public String imageFilePath(String attachmentId) {
//        return cmmtCodeDao.imageFilePath(attachmentId);
//    }

    /**
     * 한개 파일 다운로드
     */
    public void downloadFile_contentType(HttpServletResponse response, String attachmentId) {
        attachmentService.downloadFile_contentType(response, config.getDir().getUpload(), attachmentId);
    }

    /**
     * 여러파일 Zip으로 다운로드
     */
    public void fileDownload_groupfiles(HttpServletResponse response, String attachmentGroupId) {
        attachmentService.downloadGroupFiles(response, config.getDir().getUpload(), attachmentGroupId, "groupFilsZip");
    }

    /**
     * 한개 파일 업로드(attachmentGroupId에 추가업로드)
     */
    public void uploadFile_toGroup(MultipartFile fileList, String attachmentGroupId) {
        // 파일 하나 업로드하는데 attachmentGroupId 로 해서  cmmt_attachment 테이블에 데이터를 넣어준다.
        if (fileList != null) {
            CmmtAtchmnfl fileGroupInfo = attachmentService.uploadFile_toGroup(config.getDir().getUpload(), attachmentGroupId, fileList, allowedFileExts, 0);
            String AttachmentGroupId = fileGroupInfo.getAttachmentGroupId();
        }
    }

    /**
     * 여러파일 파일 업로드(attachmentGroupId에 추가업로드)
     */
    public void uploadFiles_toGroup(List<MultipartFile> fileList, String attachmentGroupId) {
        // 파일 여러개를 업로드하는데 attachmentGroupId 로 해서  cmmt_attachment 테이블에 데이터를 넣어준다.
        if (fileList != null && fileList.size() > 0) {
            List<CmmtAtchmnfl> fileGroupInfo = attachmentService.uploadFiles_toGroup(config.getDir().getUpload(), attachmentGroupId, fileList, allowedFileExts, 0);
        }
    }

    /**
     * 한개 파일 업로드
     */
    public String uploadFile_toNewGroup(MultipartFile fileList) {
        // 파일 하나 업로드하는데 cmmt_attachment_group 테이블과 cmmt_attachment 테이블에 데이터를 넣어준다.
        if (fileList != null) {
            CmmtAtchmnfl fileGroupInfo = attachmentService.uploadFile_toNewGroup(config.getDir().getUpload(), fileList, allowedFileExts, 0);
            String AttachmentGroupId = fileGroupInfo.getAttachmentGroupId();
            String AttachmentId = fileGroupInfo.getAttachmentId();
            return AttachmentGroupId;
        }
        throw new InvalidationException(String.format(TspCode.validateMessage.DB오류, "데이터"));
    }

    /**
     * 여러파일 파일 업로드
     */
    public String uploadFiles_toNewGroup(List<MultipartFile> fileList) {
        // 파일 여러개를 업로드하는데 cmmt_attachment_group 테이블과 cmmt_attachment 테이블에 데이터를 넣어준다.
        if (fileList != null && fileList.size() > 0) {
            CmmtAtchmnflGroup fileGroupInfo = attachmentService.uploadFiles_toNewGroup(config.getDir().getUpload(), fileList, allowedFileExts, 0);
            String AttachmentGroupId = fileGroupInfo.getAttachmentGroupId();
            return AttachmentGroupId;
        }
        throw new InvalidationException(String.format(TspCode.validateMessage.DB오류, "데이터"));
    }

    /**
     * DB와 파일 삭제
     */
    public void removeFile(String attachmentId) {
        attachmentService.removeFile(config.getDir().getUpload(), attachmentId);
    }

}