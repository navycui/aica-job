package aicluster.mvn.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.mvn.common.entity.UsptResrceAsgnHist;

@Repository
public interface UsptResrceAsgnHistDao {
	void insert(UsptResrceAsgnHist hist);
	List<UsptResrceAsgnHist> selectList(String alrsrcId);
}
