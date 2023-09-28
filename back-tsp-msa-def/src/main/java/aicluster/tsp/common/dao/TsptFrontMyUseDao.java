package aicluster.tsp.common.dao;

import aicluster.tsp.api.common.param.CommonAttachmentParam;
import aicluster.tsp.api.front.mypage.use.param.*;
import aicluster.tsp.common.dto.*;
import aicluster.tsp.common.entity.TsptEqpmnExtn;
import aicluster.tsp.common.entity.TsptEqpmnReprt;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TsptFrontMyUseDao {

    /** 장비사용 목록 조회 */
    // 사용요청 전체 건수 확인
    long selectMyUseCnt(@Param("param") MyUseListSearchParam param);
    // 사용요청 목록 조회
    List<FrontMyUseListDto> selectMyUseList(Long beginRowNum, Long itemsPerPage, @Param("param") MyUseListSearchParam param);
    /** 장비사용 상세정보(승인전, 후) 조회 및 첨부파일 ID 조회 */
    // 사용요청 상세정보 승인 전
    MyUseDetailParam selectMyUseDetail(String reqstId);
    // 서약서 정보 조회
    CommonAttachmentParam selectPromsFileInfo(String atchmnflGroupId);
    // 사용요청 상세정보 승인 전 첨부파일 개당 다운로드 리스트 조회
    List<MyUseDetailParam.MyAttachMentParam> selectMyUseDetailAttachMentParam(String atchmnflGroupId);
    // 사용요청 상세정보 승인 후
    MyUseDetailParam.MyUseApproveParam selectApproveParam(String reqstId);
    // 사용요청 상세정보 승인 후 반입 여부 확인
    MyUseDetailParam.MyUseApproveParam.MyUseDetailTkinAtParam selectTkinParam(String reqstId, String processKnd);
    /** 장비사용 신청취소 */
    void putUseCancel(String reqstId, String reqstSttus);
    /** 장비사용 사용종료 */
    void putUseEnduse(String reqstId, String useSttus);
    /** 장비사용 상세 기간연장목록 조회 */
    List<FrontMyUseExtndListDto> getUseExtndList(String reqstId);

    /** 장비사용 상세 기간연장 조회 장비ID 조회 */
    String getEqpmnId(String reqstId);
    /** 장비사용 상세 기간연장 조회 */
    List<List<Object>> getUseExtndReqst(String reqstId, String reqstSttus, String eqpmnId, String searchMonth);
    /** 장비사용 상세 기간연장 신청시 정보조회 */
    MyUseExtndParam getUseExtndReqstInfo(String reqstId);
    /** 장비사용 상세 기간연장 신청 */
    void postUseExtndReqst(TsptEqpmnExtn tsptEqpmnExtn);
    /** 장비사용 상세 사용료부과내역 */
    List<FrontMyUseExtndRntfeeDto.MyRntfee> getUseRntfee(String reqstId, String reqstSttus);
    List<FrontMyUseExtndRntfeeDto.MyAddRntfee> getUseAddRntfee(String reqstId, String reqstSttus);

    /** 장비사용 상세 결과 보고서 정보 조회 */
    FrontMyUseReprtDto getUseReprtInfo(String reqstId);
    FrontMyUseReprtDto.MyUseReprt getUseReprt(String reprtId);
    void postUseReprt(@Param("param") TsptEqpmnReprt tsptEqpmnReprt);
    void putUseReprt(TsptEqpmnReprt tsptEqpmnReprt);

}
