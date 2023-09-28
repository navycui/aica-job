package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptAcdmcr;

@Repository
public interface UsptAcdmcrDao {
	void insert(UsptAcdmcr info);
	int update(UsptAcdmcr info);
	int delete(UsptAcdmcr info);
	List<UsptAcdmcr> selectList(String memberId);
}
