package aicluster.tsp.common.dao;

import aicluster.tsp.api.front.mypage.resrce.param.MyResrceDetailParam;
import aicluster.tsp.api.front.mypage.resrce.param.MyResrceSearchParam;
import aicluster.tsp.common.entity.TsptApplcnt;
import aicluster.tsp.common.entity.TsptResrceUseReqst;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TsptFrontMyResrceDao {

    /** 실증자원사용 목록 List조회 */
    Long getResrceCnt(@Param("param") MyResrceSearchParam param);
    List<TsptResrceUseReqst> getResrceList(Long beginRowNum, Long itemsPerPage, @Param("param") MyResrceSearchParam param);
    /** 실증자원사용 상세정보 조회 */
    TsptResrceUseReqst getResrceInfo(String reqstId);
    TsptApplcnt getTsptApplcnt(String applcntId);
    List<MyResrceDetailParam.MyAttachMentParam> selectMyResrceDetailAttachMentParam(String atchmnflGroupId);
    /** 실증자원사용 처리 */
    void putResrceDetail(String reqstId, String sttus);
}
