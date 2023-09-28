package aicluster.tsp.common.dao;

import aicluster.tsp.api.front.anals.param.FrontAnalsReqstParam;
import aicluster.tsp.api.front.anals.param.FrontAnalsTmplatParam;
import aicluster.tsp.api.front.mypage.anals.param.FrontMypageAnalsReqstParam;
import aicluster.tsp.common.dto.FrontMyAnalsReqstDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TsptFrontAnalsReqstDao {

    // 사용신청 전체 건수 확인
    long selectMyAnalsCnt(@Param("param") FrontMypageAnalsReqstParam param);
    // 사용신청 목록 조회
    List<FrontMyAnalsReqstDto> selectMyAnalsList(Long beginRowNum, Long itemsPerPage, @Param("param") FrontMypageAnalsReqstParam param);

    FrontMyAnalsReqstDto selectMyAnalsDetail(String reqstId);
    // 분석환경 신청취소
    void updateMyAnalsCancel(String reqstId, String useSttus, String memberId);

    Long selectAnalsReqst(FrontAnalsReqstParam param);
    void insertAnalsReqst(FrontAnalsReqstParam param);

    List<FrontAnalsTmplatParam> selectAnalsTmplat(FrontAnalsTmplatParam param);
}
