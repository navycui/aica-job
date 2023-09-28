package aicluster.tsp.common.dao;

import aicluster.tsp.api.front.svcintro.param.EqpmnIntroDetailParam;
import aicluster.tsp.api.front.svcintro.param.EqpmnIntroParam;
import aicluster.tsp.api.front.svcintro.param.EqpmnIntroSearchParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TsptFrontEqpmnDao {

    long selectEqpmnInfoCount(@Param("search") EqpmnIntroSearchParam search);

    List<EqpmnIntroParam> selectEqpmnInfoList(@Param("search") EqpmnIntroSearchParam search,
            Long beginRowNum,
            Long itemsPerPage,
            boolean isExcel);

    EqpmnIntroDetailParam selectEqpmnInfoDetail(String eqpmnId);
}
