package aicluster.tsp.common.dao;

import aicluster.tsp.api.admin.eqpmn.dscnt.param.DscntApplyParam;
import aicluster.tsp.api.admin.eqpmn.dscnt.param.DscntParam;
import aicluster.tsp.common.dto.EqpmnDscntListDto;
import aicluster.tsp.common.entity.TsptEqpmnDscnt;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TsptEqpmnDscntDao {
    void postDscntList(TsptEqpmnDscnt tsptEqpmnDscnt);

    List<DscntParam> searchDscntList(String search);

    // 전체 건수 확인
    long selectDscntCount(@Param("search") DscntParam search);

    List<DscntParam> selectDscntList(@Param("search") DscntParam search, Long beginRowNum, Long itemsPerPage);

    //할인 적용 검색
    long selectApplyCount(@Param("apply") DscntParam apply);

    List<DscntParam> selectApplyList(@Param("apply") DscntParam apply, Long beginRowNum, Long itemsPerPage);

    void updateUsgstt(DscntParam dscntParam);
    
    //장비 할인 적용
    void postDscntApplyList(DscntApplyParam param);

    List<EqpmnDscntListDto> selectEqpmnDscntList(String eqpmnId);

}
