package aicluster.tsp.common.dao;

import aicluster.tsp.api.admin.resrce.param.ResrceDetailParam;
import aicluster.tsp.api.admin.resrce.param.ResrceListParam;
import aicluster.tsp.common.dto.ResrceUseReqstDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TsptResrceUseReqstDao {

    long selectResrceApplyCount(@Param("search") ResrceListParam param);

    List<ResrceUseReqstDto> selectResrceApplyList(
            @Param("search") ResrceListParam param,
            Long beginRowNum,
            Long itemsPerPage,
            boolean isExcel);
    List<ResrceUseReqstDto> selectResrceApplyList(
            @Param("search") ResrceListParam param,
            @Param("isExcel")boolean isExcel
    );

    long selectResrceUseCount(@Param("search") ResrceListParam param);

    List<ResrceUseReqstDto> selectResrceUseList(
            @Param("search") ResrceListParam param,
            @Param("isExcel") boolean ixExcel
    );

    List<ResrceUseReqstDto> selectResrceUseList(
            @Param("search") ResrceListParam param,
            Long beginRowNum,
            Long itemsPerPage,
            boolean isExcel);

    ResrceDetailParam selectResrceDetail(String reqstId);

    ResrceDetailParam selectResrceUseDetail(String reqstId);

    void putApprove(String reqstId);

    void putSpmRequest(String reqstId, String rsndqf);

    void putReject(String reqstId);

    void putCancel(String reqstId);

    void putReturn(String reqstId);

    void putReqreturn(String reqstId);
}
