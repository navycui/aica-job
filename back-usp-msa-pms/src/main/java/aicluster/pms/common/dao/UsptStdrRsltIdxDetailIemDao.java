package aicluster.pms.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptStdrRsltIdxDetailIem;

@Repository
public interface UsptStdrRsltIdxDetailIemDao {
	void insert(UsptStdrRsltIdxDetailIem info);
	int update(UsptStdrRsltIdxDetailIem info);
	int delete(UsptStdrRsltIdxDetailIem info);
	int deleteAll(String rsltIdxId);
}
