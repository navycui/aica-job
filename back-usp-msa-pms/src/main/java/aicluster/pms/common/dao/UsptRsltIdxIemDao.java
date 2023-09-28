package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptRsltIdxIem;

@Repository
public interface UsptRsltIdxIemDao {
	void insert(UsptRsltIdxIem info);
	int update(UsptRsltIdxIem info);
	UsptRsltIdxIem select(String rsltIdxIemId);
	List<UsptRsltIdxIem> selectList(String rsltId);
}
