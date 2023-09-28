package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptExpertAcdmcr;

@Repository
public interface UsptExpertAcdmcrDao {

	List<UsptExpertAcdmcr> selectList(UsptExpertAcdmcr inputParam);

	int insert(UsptExpertAcdmcr inputParam);

	int update(UsptExpertAcdmcr inputParam);

	int delete(UsptExpertAcdmcr inputParam);

	int deleteExpert(String  expertId);
}
