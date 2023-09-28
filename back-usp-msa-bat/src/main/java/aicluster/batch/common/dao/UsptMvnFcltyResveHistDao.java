package aicluster.batch.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.batch.common.entity.UsptMvnFcltyResveHist;

@Repository
public interface UsptMvnFcltyResveHistDao {
	void insertList(List<UsptMvnFcltyResveHist> list);
}
