package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptRsltIdxIemCn;

@Repository
public interface UsptRsltIdxIemCnDao {
	void insert(UsptRsltIdxIemCn info);
	int update(UsptRsltIdxIemCn info);
	int updateDeleteAt(UsptRsltIdxIemCn info);
	UsptRsltIdxIemCn select(String rsltIdxIemCnId);
	List<UsptRsltIdxIemCn> selectList(String rsltIdxIemId);
}
