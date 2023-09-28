package aicluster.pms.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptTaskRspnber;

@Repository
public interface UsptTaskRspnberDao {
	int delete(UsptTaskRspnber usptTaskRspnber);

	int insert(UsptTaskRspnber usptTaskRspnber);

	int update(UsptTaskRspnber usptTaskRspnber);

	int updateByBsnsPlanDocId(UsptTaskRspnber usptTaskRspnber);

	UsptTaskRspnber selectOne(UsptTaskRspnber usptTaskRspnber);

}
