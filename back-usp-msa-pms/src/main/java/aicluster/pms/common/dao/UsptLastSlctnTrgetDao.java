package aicluster.pms.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptLastSlctnTrget;

@Repository
public interface UsptLastSlctnTrgetDao {
	void insert(UsptLastSlctnTrget info);
}
