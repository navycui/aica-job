package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptExpertCrqfc;

@Repository
public interface UsptExpertCrqfcDao {

	List<UsptExpertCrqfc> selectList(UsptExpertCrqfc inputParam);

	int insert(UsptExpertCrqfc inputParam);

	int update(UsptExpertCrqfc inputParam);

	int delete(UsptExpertCrqfc inputParam);

	int deleteExpert(String  expertId);
}
