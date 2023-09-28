package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptEvlStep;

@Repository
public interface UsptEvlStepDao {

	UsptEvlStep select(String evlStepId);

	int insert(UsptEvlStep usptEvlStep);

	int update(UsptEvlStep usptEvlStep);

	int delete(UsptEvlStep usptEvlStep);

	void deleteBySectId(String sectId);

	List<UsptEvlStep> selectExistList(UsptEvlStep usptEvlStep);

	List<UsptEvlStep> selectList(@Param("evlPlanId") String evlPlanId, @Param("sortOrdrNo") String sortOrdrNo);
}
