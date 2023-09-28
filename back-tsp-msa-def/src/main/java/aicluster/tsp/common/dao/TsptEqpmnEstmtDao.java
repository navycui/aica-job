package aicluster.tsp.common.dao;

import aicluster.tsp.api.admin.eqpmn.estmt.param.EstmtDetailParam;
import aicluster.tsp.api.admin.eqpmn.estmt.param.EstmtDetailParam.EstmtDetailModifyPrice;
import aicluster.tsp.api.admin.eqpmn.estmt.param.EstmtListParam;
import aicluster.tsp.api.admin.eqpmn.estmt.param.EstmtOzReportParam;
import aicluster.tsp.api.admin.eqpmn.estmt.param.EstmtProcessParam;
import aicluster.tsp.common.dto.EqpmnEstmtDetailDto;
import aicluster.tsp.common.dto.EqpmnEstmtListDto;
import aicluster.tsp.common.dto.EqpmnUseDscntDto;
import aicluster.tsp.common.entity.TsptEqpmnEstmtReqstHist;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TsptEqpmnEstmtDao {
	// 전체 건수 확인

	// 페이지 조회 엑셀 다운로드
	List<EqpmnEstmtListDto> selectEquipmentEstmtList(boolean isExcel, @Param("estmtListParam") EstmtListParam estmtListParam);
	// 견적요청 상세 > 신청정보
	EqpmnEstmtDetailDto selectEqpmnEstmtDetail(String eqpmnEstmtId);

	@Select("select usgtm, dscnt_id from tsp_api.tspt_eqpmn_estmt_reqst where estmt_id = #{estmtId}")
	EstmtDetailModifyPrice selectEqpmnEstmtDetailModifyPrice(String estmtId);
	//void updateEqpmnEstmtDetailModifyPrice(EstmtDetailParam.EstmtDetailModifyPrice estmtDetailModifyPrice);
	void updateEqpmnEstmtDetailModifyPrice(EstmtDetailParam.EstmtDetailModifyPrice estmtDetailModifyPrice);
	// 견적요청 상세 > 조정 사용금액 > 할인 List

	long selectEqpmnEstmtCount(@Param("estmtListParam") EstmtListParam estmtListParam);

	List<EqpmnEstmtListDto> selectEqpmnEstmtList(boolean isExcel, Long beginRowNum, Long itemsPerPage,
												 @Param("estmtListParam") EstmtListParam estmtListParam);

	@Select("select count(hist_id) from tsp_api.tspt_eqpmn_estmt_reqst_hist where estmt_id = #{estmtId}")
	long selectEqpmnEstmtHistCount(String estmtId);
	List<TsptEqpmnEstmtReqstHist> selectEqpmnEstmtHistList(String estmtId, boolean isExcel, Long beginRowNum, Long itemsPerPage);

	@Select("select estmt_id, reqst_sttus, rsndqf from tsp_api.tspt_eqpmn_estmt_reqst where estmt_id = #{estmtId}")
	EstmtProcessParam selectEqpmnEstmtProcess(String estmtId);
	//장비견적 신청 이후 사용신청 여부 확인
	@Select("SELECT reqst_id FROM tsp_api.tspt_eqpmn_use_reqst WHERE rcept_no = (SELECT rcept_no FROM tsp_api.tspt_eqpmn_estmt_reqst WHERE estmt_id = #{estmtId})")
	String selectReqstId(String estmtId);

	void updateEqpmnEstmtProcess(EstmtProcessParam param);

	void insertEqpmnEstmtHist(TsptEqpmnEstmtReqstHist param);

	EqpmnUseDscntDto selectEstmtDscnt(String estmtId, String dscntId);

	void updateEstmtDscnt(String estmtId, String dscntId, String updusrId,
						  Integer rntfee, Integer usgtm, Integer dscntAmount);

	EstmtOzReportParam selectEstmtAdminInfo(String estmtId);
	//견적서 발급 일자 조회
	@Select("SELECT creat_dt FROM tsp_api.tspt_eqpmn_estmt_reqst_hist WHERE estmt_id = #{estmtId} AND process_knd = 'EST_APPROVE'")
	Date selectCreatDt(String estmtId);
	//신청자 정보 조회
	@Select("SELECT applcnt_id FROM tsp_api.tspt_eqpmn_estmt_reqst WHERE estmt_id = #{estmtId}")
	String selectApplcntId(String estmtId);
}
