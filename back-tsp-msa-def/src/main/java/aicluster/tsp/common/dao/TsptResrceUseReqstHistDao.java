package aicluster.tsp.common.dao;

import aicluster.tsp.api.admin.resrce.param.ResrceHistParam;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TsptResrceUseReqstHistDao {

    Long getResrceHistCount(String reqstId);

    List<ResrceHistParam> getResrceHist(String reqstId, Long beginRowNum, Long itemsPerPage);

    Long getResrceHistUseCount(String reqstId);

    List<ResrceHistParam> getResrceUseHist(String reqstId, Long beginRowNum, Long itemsPerPage);

    void putResrceHist(ResrceHistParam param);

}
