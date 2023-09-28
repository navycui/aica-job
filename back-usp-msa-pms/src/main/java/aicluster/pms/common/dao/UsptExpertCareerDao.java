package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptExpertCareer;

@Repository
public interface UsptExpertCareerDao {

	List<UsptExpertCareer> selectList(UsptExpertCareer inputParam);

	int insert(UsptExpertCareer inputParam);

	int update(UsptExpertCareer inputParam);

	int delete(UsptExpertCareer inputParam);

	int deleteExpert(String  expertId);
}
