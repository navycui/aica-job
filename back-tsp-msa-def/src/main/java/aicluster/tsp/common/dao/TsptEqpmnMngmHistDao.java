package aicluster.tsp.common.dao;

import aicluster.tsp.api.admin.eqpmn.mngm.param.MngmMgtHistParam;

import aicluster.tsp.common.entity.TsptEqpmnProcessHist;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TsptEqpmnMngmHistDao {
	//
	void insertEqpmnHist(TsptEqpmnProcessHist tsptEqpmnProcessHist);
	//장비처리이력 전체 카운팅
	long selectProHistCnt(@Param("eqpmnId") String eqpmnId);
	//장비처리이력 조회
    List<TsptEqpmnProcessHist> selectProHistList(
			@Param("eqpmnId") String eqpmnId,
			@Param("beginRowNum") Long beginRowNum,
			@Param("itemsPerPage") Long itemsPerPage
	);
	// 장비관리이력 전체 카운팅
	long selectMgtHistCount(
			@Param("eqpmnId") String eqpmnId,
			@Param("manageDiv") String manageDiv)
	;
	// 장비관리이력 조회
	List<MngmMgtHistParam> selectMgtHistList(
			@Param("eqpmnId") String eqpmnId,
			@Param("manageDiv") String manageDiv,
			@Param("isExcel") Boolean isExcel,
			@Param("beginRowNum") Long beginRowNum,
			@Param("itemsPerPage") Long itemsPerPage);
	// 장비관리이력 엑셀 다운로드
	List<MngmMgtHistParam> selectMgtHistList(
			@Param("eqpmnId") String eqpmnId,
			@Param("manageDiv") String manageDiv,
			@Param("isExcel") Boolean isExcel);
//	List<TsptEqpmnHist> selectEquipmentHistoryList(@Param("eqpmnId") String eqpmnId);
//	int insertEquipmentHistory(TsptEqpmnHist tsptEqpmnHist);
//	TsptEqpmnHist selectEqpmnRegistConfirm(String eqpmnId, String MgtType);
//	void insertEqpmnInfoHist(TsptEqpmnHist tsptEqpmnHist);
}
