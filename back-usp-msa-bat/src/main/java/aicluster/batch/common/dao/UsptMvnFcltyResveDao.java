package aicluster.batch.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.batch.common.entity.UsptMvnFcltyResve;

@Repository
public interface UsptMvnFcltyResveDao {
	List<UsptMvnFcltyResve> selectList();
	void updateList(List<UsptMvnFcltyResve> list);
}
