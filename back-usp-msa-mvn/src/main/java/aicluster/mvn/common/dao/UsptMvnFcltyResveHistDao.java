package aicluster.mvn.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.mvn.common.entity.UsptMvnFcltyResveHist;

@Repository
public interface UsptMvnFcltyResveHistDao {
	List<UsptMvnFcltyResveHist> selectList(String reserveId);

	void insert(UsptMvnFcltyResveHist hist);
}
