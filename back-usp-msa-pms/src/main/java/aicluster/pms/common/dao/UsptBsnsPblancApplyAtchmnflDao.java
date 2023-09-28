package aicluster.pms.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptBsnsPblancApplyAtchmnfl;

@Repository
public interface UsptBsnsPblancApplyAtchmnflDao {

	int update(UsptBsnsPblancApplyAtchmnfl info);

	String selectAtchmnflSetupId(UsptBsnsPblancApplyAtchmnfl info);

}
