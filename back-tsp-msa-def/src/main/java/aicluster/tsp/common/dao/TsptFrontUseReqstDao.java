package aicluster.tsp.common.dao;

import aicluster.tsp.api.front.usereqst.estmt.param.UseReqstEstmtParam;
import aicluster.tsp.api.front.usereqst.estmt.param.UseReqstEstmtSelectParam;
import aicluster.tsp.api.front.usereqst.estmt.param.UseReqstEstmtUseDateParam;
import aicluster.tsp.common.dto.FrontEqpmnSelectDto;
import aicluster.tsp.common.dto.FrontEqpmnSelectListDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TsptFrontUseReqstDao {

    long selectUseReqstEqpmnListCnt(@Param("param") UseReqstEstmtSelectParam param);
    // 장비 목록 조회
    List<FrontEqpmnSelectListDto> selectUseReqstEqpmnList(Long beginRowNum, Long itemsPerPage, @Param("param") UseReqstEstmtSelectParam param);

    // 장비 상세정보 조회
    FrontEqpmnSelectDto selectUseReqstEqpmnInfo(String eqpmnId);

    List<UseReqstEstmtUseDateParam> selectEqpmnUseDateList(UseReqstEstmtUseDateParam param);

    void insertEstmt(UseReqstEstmtParam param);

    void insertUseReqst(UseReqstEstmtParam param);
}
