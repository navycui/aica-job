package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptBsnsRsltIdxStdIem;

@Repository
public interface UsptBsnsRsltIdxStdIemDao {
	void insert(UsptBsnsRsltIdxStdIem info);
	void insertList(List<UsptBsnsRsltIdxStdIem> list);
	int update(UsptBsnsRsltIdxStdIem info);
	int delete(UsptBsnsRsltIdxStdIem info);
	int deleteAll(String rsltIdxId);
}
