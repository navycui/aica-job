package aicluster.mvn.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.mvn.common.entity.UsptMvnEtHist;

@Repository
public interface UsptMvnEtHistDao {
	void insert(UsptMvnEtHist usptMvnEtHist);
	List<UsptMvnEtHist> selectList(String mvnEtReqId);
}
