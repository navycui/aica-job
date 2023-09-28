package aicluster.batch.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.batch.common.entity.UsptMvnChcktReqst;

@Repository
public interface UsptMvnChcktReqstDao {
	void update(UsptMvnChcktReqst usptCheckoutReq);

	List<UsptMvnChcktReqst> selectList_mvnCheckoutYn();
}
