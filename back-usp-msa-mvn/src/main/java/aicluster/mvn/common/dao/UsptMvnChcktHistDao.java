package aicluster.mvn.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.mvn.common.entity.UsptMvnChcktHist;

@Repository
public interface UsptMvnChcktHistDao {
	void insert(UsptMvnChcktHist hist);

	List<UsptMvnChcktHist> selectList(String checkoutReqId);
}
