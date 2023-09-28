package aicluster.pms.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptStdrRsltIdxStdIem;

@Repository
public interface UsptStdrRsltIdxStdIemDao {
	void insert(UsptStdrRsltIdxStdIem info);
	int update(UsptStdrRsltIdxStdIem info);
	int delete(UsptStdrRsltIdxStdIem info);
	int deleteAll(String rsltIdxId);
}
