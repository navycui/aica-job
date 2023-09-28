package aicluster.tsp.common.dao;

import aicluster.tsp.api.admin.eqpmn.reprt.param.ReprtParam;
import aicluster.tsp.api.admin.eqpmn.reprt.param.ReprtProcessParam;
import aicluster.tsp.common.dto.EqpmnReprtDetailDto;
import aicluster.tsp.common.dto.EqpmnReprtDto;
import aicluster.tsp.common.entity.TsptEqpmnReprtHist;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TsptEqpmnReprtDao {
	long selectEqpmnReprtCount(@Param("param") ReprtParam param);

	List<EqpmnReprtDto> selectEqpmnReprtList(@Param("param") ReprtParam param,
											 Long beginRowNum,
											 Long itemsPerPage,
											 boolean isExcel);

	EqpmnReprtDetailDto selectEqpmnReprtDetail(String reprtId);


	@Select("select count(hist_id) from tsp_api.tspt_eqpmn_reprt_hist where reprt_id = #{reprtId}")
	long selectEqpmnReprtHistCount(String reprtId);

	List<TsptEqpmnReprtHist> selectEqpmnReprtHistList(String reprtId, boolean isExcel, Long beginRowNum, Long itemsPerPage);

	@Select("select reprt_sttus, rsndqf from tsp_api.tspt_eqpmn_reprt where reprt_id = #{reqetId}")
	ReprtProcessParam selectReqstProcess(String reqetId);

	void updateReprtProcess(ReprtProcessParam param);

	void insertReprtHist(TsptEqpmnReprtHist param);
}
