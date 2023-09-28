package aicluster.tsp.common.dao;

import aicluster.tsp.common.entity.TsptEqpmnCl;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TsptEqpmnClDao {
	List<TsptEqpmnCl> selectCatrgoryList(String eqpmnClId);
	List<TsptEqpmnCl> selectCatrgoryAllList();
	List<TsptEqpmnCl> selectChildParamList(String eqpmnClId);
	Integer selectEqpmnCount(String eqpmnClId);
	Integer selectCategoryLevel(String eqpmnClId);
	Integer selectEqpmnClIdCount(String eqpmnClId);
	void insertCategory(@Param("list") List<TsptEqpmnCl> equipmentCategoryList);
	int deleteCategory(String eqpmnClId);
	int updateSoreOrder(String eqpmnLclasId);

	String selectCategoryCheck(String eqpmnClNm, String eqpmnLclasId);
}
