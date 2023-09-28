package aicluster.batch.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.batch.common.entity.UsptMvnChcktHist;

@Repository
public interface UsptMvnChcktHistDao {
	void insertList(List<UsptMvnChcktHist> list);
}
