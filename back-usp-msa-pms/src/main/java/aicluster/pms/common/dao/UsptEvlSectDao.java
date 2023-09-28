package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptSect;

@Repository
public interface UsptEvlSectDao {
	List<UsptSect> selectList(String evlPlanId);

	List<UsptSect> selectExistList(UsptSect usptSect);

	int insert(UsptSect usptSectList);
	int update(UsptSect usptSectList);
	int delete(String sectId);
}
