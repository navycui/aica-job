package aicluster.tsp.common.dao;

import aicluster.tsp.api.admin.eqpmn.EqpmnAllListParam;
import aicluster.tsp.api.admin.eqpmn.EqpmnParam;
import aicluster.tsp.api.admin.eqpmn.mngm.param.MngmStatusParam;
import aicluster.tsp.api.admin.eqpmn.mngm.param.MngmSearchParam;
import aicluster.tsp.common.dto.EqpmnAllListDto;
import aicluster.tsp.common.entity.TsptEqpmn;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TsptEqpmnDao {
	// 전체 건수 확인
	long selectEquipmentCount(@Param("param") EqpmnAllListParam eqpmnAllListParam);
	// 페이지 목록 조회
	List<EqpmnAllListDto> selectEquipmentList(
			@Param("param") EqpmnAllListParam eqpmnAllListParam,
			@Param("isExcel") Boolean isExcel,
			@Param("beginRowNum") Long beginRowNum,
			@Param("itemsPerPage") Long itemsPerPage
			);
	// 페이지 조회 엑셀 다운로드
	List<EqpmnAllListDto> selectEquipmentList(
			@Param("param") EqpmnAllListParam eqpmnAllListParam,
			@Param("isExcel") Boolean isExcel
	);

	// 전체 건수 확인
	long selectEqpmnStatusCount(String status, @Param("search") MngmSearchParam search);
	// 장비 상태에 값에 따른 정보
	List<MngmStatusParam> selectEqpmnStatusList(String status, boolean isExcel, @Param("search") MngmSearchParam search);
	List<MngmStatusParam> selectEqpmnStatusList(String status, @Param("search") MngmSearchParam search, Long beginRowNum, Long itemsPerPage, boolean isExcel);

	void deleteEquipment(String id);
	void insertEquipment(EqpmnParam param);
	int updateEquipment(TsptEqpmn tsptEqpmn);
	String selectEquipmentImageId(String eqpmnId);
}
