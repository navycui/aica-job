package aicluster.tsp.common.dao;

import aicluster.tsp.api.admin.eqpmn.use.param.EqpmnUseReqstParam;
import aicluster.tsp.api.admin.eqpmn.use.param.UseReqstProcessParam;
import aicluster.tsp.api.admin.eqpmn.use.param.UseReqstTkoutProcessParam;
import aicluster.tsp.common.dto.EqpmnUseDscntDto;
import aicluster.tsp.common.dto.EqpmnUseReqstDetailDto;
import aicluster.tsp.common.dto.EqpmnUseReqstDto;
import aicluster.tsp.common.entity.TsptEqpmnUseReqst;
import aicluster.tsp.common.entity.TsptEqpmnUseReqstHist;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TsptEqpmnUseReqstDao {
	long selectRentalIsEqpmnCount(String eqpmnId);
	long selectEqpmnReqstCount(@Param("param") EqpmnUseReqstParam param);

	List<EqpmnUseReqstDto> selectEqpmnReqstList(@Param("param") EqpmnUseReqstParam param,
													 Long beginRowNum,
													 Long itemsPerPage,
													 boolean isExcel);



	EqpmnUseReqstDetailDto selectEqpmnUseReqstDetail(String reqstId);

	@Select("select reqst_sttus, rsndqf, applcnt_id, eqpmn_id, use_begin_dt, use_end_dt  from tsp_api.tspt_eqpmn_use_reqst where reqst_id = #{reqstId}")
	UseReqstProcessParam selectUseReqstProcess(String reqstId);

	@Select("select reqst_sttus, use_sttus from tsp_api.tspt_eqpmn_use_reqst where reqst_id = #{reqstId}")
	TsptEqpmnUseReqst selectUseReqstConsent(String reqstId);

	void updateUseReqstProcess(UseReqstProcessParam param);

	void updateUseReqstConsent(String reqstId, String reqstSttus, String useSttus, String updusrId);

	void updateUseReqstTkoutProcess(UseReqstTkoutProcessParam param);

	void updateUseReqstDscnt(String reqstId, String dscntId, String updusrId,
							 Integer rntfee, Integer usgtm, Integer dscntAmount);

	@Select("select count(hist_id) from tsp_api.tspt_eqpmn_use_reqst_hist where reqst_id = #{reqstId}")
	long selectUseReqstHistCount(String reqstId);

	List<TsptEqpmnUseReqstHist> selectUseReqstHistList(String reqstId,
													   Long beginRowNum,
													   Long itemsPerPage,
													   boolean isExcel);
	void insertUseReqstHist(TsptEqpmnUseReqstHist param);

	EqpmnUseDscntDto selectUseDscnt(String reqstId, String dscntId);
	String checkEstmtId(String applcntId, String eqpmnId, Date useBeginDt, Date useEndDt);
}
