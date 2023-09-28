package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptBsnsRsltIdxDetailIem;

@Repository
public interface UsptBsnsRsltIdxDetailIemDao {
	void insert(UsptBsnsRsltIdxDetailIem info);
	void insertList(List<UsptBsnsRsltIdxDetailIem> list);
	int update(UsptBsnsRsltIdxDetailIem info);
	int delete(UsptBsnsRsltIdxDetailIem info);
	int deleteAll(String rsltIdxId);
}
