package aicluster.tsp.common.dao;

import aicluster.tsp.api.front.mypage.estmt.param.MyEstmtDetailParam;
import aicluster.tsp.api.front.mypage.estmt.param.MyEstmtDwldInfoParam;
import aicluster.tsp.api.front.mypage.estmt.param.MyEstmtListSearchParam;
import aicluster.tsp.common.dto.FrontMyEstmtListDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TsptFrontMyEstmtDao {

    // 사용신청 전 견적 정보 조회하기 위한 견적ID 조회
    long checkMyEstmtId(String eqpmnId, String mberId);
    // 사용신청 전 견적 정보 조회하기 위한 견적ID 조회
    String selectMyEstmtId(String eqpmnId, String mberId);
    // 견적서발급 생성일자 조회
    MyEstmtDetailParam.EstApproveDt selectEstApprove(String estmtId, String reqstSttus);
    // 견적요청 전체 건수 확인
    long selectMyEstmtCnt(@Param("param") MyEstmtListSearchParam param);
    // 견적요청 목록 조회
    List<FrontMyEstmtListDto> selectMyEstmtList(Long beginRowNum, Long itemsPerPage, @Param("param") MyEstmtListSearchParam param);
    // 견적요청 상세정보 조회
    MyEstmtDetailParam selectMyEstmtDetail(String estmtId);
    // 견적요청 상세정보 신청취소
    int updateMyEstmtDetailCancel(String estmtId);
    // 견적요청 상세정보 첨부파일 개당 다운로드 리스트 조회
    List<MyEstmtDetailParam.MyAttachMentParam> selectMyEstmtDetailAttachMentParam(String atchmnflGroupId);
    //장비견적 신청 이후 사용신청 여부 확인
    @Select("SELECT reqst_id FROM tsp_api.tspt_eqpmn_use_reqst WHERE rcept_no = (SELECT rcept_no FROM tsp_api.tspt_eqpmn_estmt_reqst WHERE estmt_id = #{estmtId})")
    String selectReqstId(String estmtId);
    //견적서 다운로드 정보 조회
    MyEstmtDwldInfoParam selectEstmtAdminInfo(String estmtId);
}
