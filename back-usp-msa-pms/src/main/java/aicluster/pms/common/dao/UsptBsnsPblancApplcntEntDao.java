package aicluster.pms.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptBsnsPblancApplcntEnt;

@Repository
public interface UsptBsnsPblancApplcntEntDao {
	void insert(UsptBsnsPblancApplcntEnt info);
	int update(UsptBsnsPblancApplcntEnt info);
	UsptBsnsPblancApplcntEnt select(String applyId);

}
