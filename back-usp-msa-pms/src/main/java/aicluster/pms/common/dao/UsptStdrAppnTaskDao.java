package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptStdrAppnTask;

@Repository
public interface UsptStdrAppnTaskDao {
	void insert(UsptStdrAppnTask info);
	int update(UsptStdrAppnTask info);
	int delete(UsptStdrAppnTask info);
	List<UsptStdrAppnTask> selectList(String stdrBsnsCd);
}
