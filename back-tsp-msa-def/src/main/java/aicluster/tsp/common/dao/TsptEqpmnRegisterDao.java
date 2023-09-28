package aicluster.tsp.common.dao;


import aicluster.tsp.common.dto.EqpmnRegisterAllList;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TsptEqpmnRegisterDao {
	long selectEquipmentRegisterCount(
			@Param("applicantId") String applicantId,
			@Param("eqpmnNmKo") String eqpmnNmKo,
			@Param("modelNm") String modelNm,
			@Param("assetNo") String assetNo
			);
	List<EqpmnRegisterAllList> selectEquipmentRegisterList(
			@Param("applicantId") String applicantId,
			@Param("eqpmnNmKo") String eqpmnNmKo,
			@Param("modelNm") String modelNm,
			@Param("assetNo") String assetNo,
			@Param("beginRowNum") Long beginRowNum,
			@Param("itemsPerPage") Long itemsPerPage
			);
	
}