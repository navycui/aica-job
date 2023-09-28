package aicluster.tsp.common.dao;

import aicluster.tsp.api.admin.eqpmn.use.param.*;
import aicluster.tsp.common.dto.EqpmnUseDetailDto;
import aicluster.tsp.common.dto.EqpmnUseDto;
import aicluster.tsp.common.dto.EqpmnUseExtndDto;
import aicluster.tsp.common.dto.EqpmnUseRntfeeHistDto;
import aicluster.tsp.common.entity.TsptEqpmnUseReqstHist;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TsptEqpmnUseDao {

	long selectEqpmnUseCount(@Param("param") EqpmnUseParam param);

	List<EqpmnUseDto> selectEqpmnUseList(@Param("param") EqpmnUseParam param,
										 Long beginRowNum,
										 Long itemsPerPage,
										 boolean isExcel);

	EqpmnUseDetailDto selectEqpmnUseDetail(String reqstId);

	@Select("select reqst_id, reqst_sttus, rsndqf from tsp_api.tspt_eqpmn_use_reqst where reqst_id = #{reqstId}")
	UseReqstProcessParam selectUseReqstProcess(String reqstId);

	void updateUseProcess(UseReqstProcessParam param);

	@Select("select reqst_id, npy_resn from tsp_api.tspt_eqpmn_use_reqst where reqst_id = #{reqstId}")
	UseNpyProcessParam selectUseNpyProcess(String reqstId);

	String selectUseRcpmnyGdcc(String reqstId);

	@Select("select reqst_id, adit_rntfee, rqest_resn from tsp_api.tspt_eqpmn_use_reqst where reqst_id = #{reqstId}")
	UseAditRntfeeParam selectUseAditRntfee(String reqstId);

	void updateUseNpyProcess(UseNpyProcessParam param);

	void updateUseRcpmnyGdcc(UseRcpmnyGdccParam param);

	void updateUseAditRntfee(UseAditRntfeeParam param);

	void insertUseHist(TsptEqpmnUseReqstHist param);


	@Select("select count(*) from tsp_api.tspt_eqpmn_use_reqst where reqst_id = #{reqstId}")
	Integer selectUseTkin(String reqstId);

	@Update("update tsp_api.tspt_eqpmn_use_reqst set tkin_at = true, use_sttus = 'END_USE', updusr_id = #{updusrId} where reqst_id = #{reqstId}")
	void updateUseTkin(String reqstId, String updusrId);

	List<EqpmnUseRntfeeHistDto> selectRntfeeList(String reqstId);

	@Select("select count(et_reqst_id) from tsp_api.tspt_eqpmn_extn where reqst_id = #{reqstId}")
	long selectUseExtndCount(String reqstId);

	List<EqpmnUseExtndDto> selectUseExtndList(String reqstId,
											  Long beginRowNum,
											  Long itemsPerPage,
											  boolean isExcel);

	EqpmnUseExtndDto selectUseExtnd(String etReqstId);

	void updateUseExtnd(UseExtndParam param);

	void updateUseReqstCancel(String reqstId, String updusrId);

}
