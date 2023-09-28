package aicluster.tsp.common.dao;

import aicluster.tsp.api.admin.anals.param.AnalsReqstParam;
import aicluster.tsp.api.admin.anals.param.AnalsReqstProcessParam;
import aicluster.tsp.api.admin.eqpmn.use.param.UseReqstTkoutProcessParam;
import aicluster.tsp.common.dto.EqpmnUseDscntDto;
import aicluster.tsp.common.dto.*;
import aicluster.tsp.common.entity.TsptAnalsUntReqstHist;
import aicluster.tsp.common.entity.TsptEqpmnUseReqst;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TsptAnalsDao {

	long selectAnalsReqstCount(@Param("param") AnalsReqstParam param);

	List<AnalsReqstDto> selectAnalsReqstList(@Param("param") AnalsReqstParam param,
											 Long beginRowNum,
											 Long itemsPerPage,
											 boolean isExcel);



	AnalsReqstDetailDto selectAnalsReqstDetail(String reqstId);

	@Select("select use_sttus, rsndqf from tsp_api.tspt_anals_unt_reqst where reqst_id = #{reqstId}")
	AnalsReqstProcessParam selectAnalsReqstProcess(String reqstId);

	@Select("select reqst_sttus, use_sttus from tsp_api.tspt_eqpmn_use_reqst where reqst_id = #{reqstId}")
	TsptEqpmnUseReqst selectUseReqstConsent(String reqstId);

	void updateAnalsReqstProcess(AnalsReqstProcessParam param);

	void updateUseReqstConsent(String reqstId, String reqstSttus, String useSttus, String updusrId);

	void updateUseReqstTkoutProcess(UseReqstTkoutProcessParam param);

	void updateUseReqstDscnt(String reqstId, String dscntId, String updusrId,
							 Integer rntfee, Integer usgtm, Integer expectRntfee, Integer dscntAmount, Integer expectUsgtm);

	@Select("select count(hist_id) from tsp_api.tspt_anals_unt_reqst_hist where reqst_id = #{reqstId}")
	long selectAnalsReqstHistCount(String reqstId);

	List<TsptAnalsUntReqstHist> selectAnalsReqstHistList(String reqstId,
														 Long beginRowNum,
														 Long itemsPerPage,
														 boolean isExcel);
	void insertAnalsReqstHist(TsptAnalsUntReqstHist param);

	EqpmnUseDscntDto selectUseDscnt(String reqstId, String dscntId);

}
