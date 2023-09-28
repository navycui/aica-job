package aicluster.tsp.api.front.mypage.estmt.service;

import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.service.AttachmentService;
import aicluster.framework.config.EnvConfig;
import aicluster.tsp.api.common.CommonService;
import aicluster.tsp.api.front.mypage.estmt.param.MyEstmtDetailParam;
import aicluster.tsp.api.front.mypage.estmt.param.MyEstmtDwldInfoParam;
import aicluster.tsp.api.front.mypage.estmt.param.MyEstmtListSearchParam;
import aicluster.tsp.common.dao.TsptFrontMyEstmtDao;
import aicluster.tsp.common.dto.FrontMyEstmtListDto;
import aicluster.tsp.common.util.TspCode;
import aicluster.tsp.common.util.TspUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.pagination.CorePagination;
import bnet.library.util.pagination.CorePaginationInfo;
import bnet.library.util.pagination.CorePaginationParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class MyEstmtService {

    @Autowired
    private TsptFrontMyEstmtDao tsptFrontMyEstmtDao;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private EnvConfig config;
    @Autowired
    private CommonService commonService;

    public int datevalue(Date cdate) {

        return Integer.parseInt(date.format(cdate,"yyyyMMdd"));
    }

    public MyEstmtDetailParam getEstmtDetailToUse(String eqpmnId){
        // 로그인 계정 셋팅
        BnMember worker = TspUtils.getMember();
        MyEstmtDetailParam dto = new MyEstmtDetailParam();

        String estmtId = tsptFrontMyEstmtDao.selectMyEstmtId(eqpmnId, worker.getMemberId());
        if (CoreUtils.string.isBlank(estmtId)){
            dto.setEstmtId("No");
//            return dto;
        }else {
            dto = tsptFrontMyEstmtDao.selectMyEstmtDetail(estmtId);
            if (CoreUtils.string.isNotBlank(dto.getAtchmnflGroupId())) {
                dto.setMyAttachMentParam(tsptFrontMyEstmtDao.selectMyEstmtDetailAttachMentParam(dto.getAtchmnflGroupId()));
            }
            if (dto.getReqstSttus().equals(TspCode.reqstSttus.EST_APPROVE.toString())) {
                dto.setEstApproveDt(tsptFrontMyEstmtDao.selectEstApprove(estmtId, dto.getReqstSttus()));
                // 견적서발급 3일 이후 견적서 폐기
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE,-3);
                if (datevalue(cal.getTime()) > datevalue(dto.getEstApproveDt().getCreatDt())) {
                    dto.setEstmtId("Disposal");
                }
            }
            long checkEstme = tsptFrontMyEstmtDao.checkMyEstmtId(eqpmnId, worker.getMemberId());
            if (checkEstme > 0){
                dto.setEstmtId("Already");
            }
            String reqstId = tsptFrontMyEstmtDao.selectReqstId(estmtId);
            if (CoreUtils.string.isNotBlank(reqstId)) {
                dto.setReqstId("Already");
            }
        }
            return dto;
    }

    public CorePagination<FrontMyEstmtListDto> getEstmtList(MyEstmtListSearchParam param, CorePaginationParam cpParam){
        // 로그인 계정 셋팅
        BnMember worker = TspUtils.getMember();

        // 검색 글자수 제한
        Integer searchString = 100;
        if (searchString > Integer.MAX_VALUE) {
            throw new InvalidationException(String.format(TspCode.validateMessage.유효오류, "데이터"));
        }
        if (CoreUtils.string.isNotBlank(param.getEqpmnNmKorean()) && param.getEqpmnNmKorean().length() > searchString){
            throw new InvalidationException("입력한 검색어가 100자를 초과하였습니다.\n검색어를 다시 입력 바랍니다.");
        }

        param.setMberId(worker.getMemberId());
        // 전체 건수 확인
        long totalItems = tsptFrontMyEstmtDao.selectMyEstmtCnt(param);

        // 조회할 페이지 구간 정보 확인
        CorePaginationInfo info = new CorePaginationInfo(cpParam.getPage(), cpParam.getItemsPerPage(), totalItems);

        // 페이지 목록 조회
        List<FrontMyEstmtListDto> list = tsptFrontMyEstmtDao.selectMyEstmtList(info.getBeginRowNum(), cpParam.getItemsPerPage(), param);

        CorePagination<FrontMyEstmtListDto> pagination = new CorePagination<>(info, list);
        // 목록 조회
        return pagination;
    }

    // 견적요청 상세정보 조회 및 첨부파일 ID 조회
    public MyEstmtDetailParam getEstmtDetail(String estmtId){
        MyEstmtDetailParam dto = tsptFrontMyEstmtDao.selectMyEstmtDetail(estmtId);

        // 1일 가용시간 계산
        dto.setUsefulHour(commonService.getUseFulHour(dto.getUsefulBeginHour(), dto.getUsefulEndHour()).getUsefulHour());

        if (CoreUtils.string.isNotBlank(dto.getAtchmnflGroupId())) {
            dto.setMyAttachMentParam(tsptFrontMyEstmtDao.selectMyEstmtDetailAttachMentParam(dto.getAtchmnflGroupId()));
        }
        if (dto.getReqstSttus().equals(TspCode.reqstSttus.EST_APPROVE.toString())) {
            dto.setEstApproveDt(tsptFrontMyEstmtDao.selectEstApprove(estmtId, dto.getReqstSttus()));
        }
        String reqstId = tsptFrontMyEstmtDao.selectReqstId(estmtId);
        if (CoreUtils.string.isNotBlank(reqstId)) {
            dto.setReqstId("Already");
        }
        return dto;
    }

    // 견적요청 상세정보 신청취소
    public MyEstmtDetailParam putEstmtDetailCancel(String estmtId){
        MyEstmtDetailParam dto = tsptFrontMyEstmtDao.selectMyEstmtDetail(estmtId);
        dto.setMyAttachMentParam(tsptFrontMyEstmtDao.selectMyEstmtDetailAttachMentParam(dto.getAtchmnflGroupId()));
        if (!(dto.getReqstSttus().equals("APPLY") || dto.getReqstSttus().equals("SPM_REQUEST"))) {
            throw new InvalidationException(String.format(TspCode.validateMessage.유효오류, "신청상태"));
        }
        tsptFrontMyEstmtDao.updateMyEstmtDetailCancel(estmtId);
        dto = getEstmtDetail(estmtId);
        return dto;

    }

    // 한개 파일 다운로드
    public void downloadFile_contentType(HttpServletResponse response, String attachmentId) {
        attachmentService.downloadFile_contentType(response, config.getDir().getUpload(), attachmentId);
    }

    // 여러파일 Zip으로 다운로드
    public void fileDownload_groupfiles(HttpServletResponse response, String attachmentGroupId) {
        attachmentService.downloadGroupFiles(response, config.getDir().getUpload(), attachmentGroupId, "groupFilsZip");
    }

    public MyEstmtDwldInfoParam getEstmtAdminInfo(String estmtId){
        BnMember worker = TspUtils.getMember();
        MyEstmtDwldInfoParam infoParam = tsptFrontMyEstmtDao.selectEstmtAdminInfo(estmtId);
        return infoParam;
    }
}
