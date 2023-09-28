package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptBsnsRsltIdx;

@Repository
public interface UsptBsnsRsltIdxDao {
	void insert(UsptBsnsRsltIdx info);
	void insertList(List<UsptBsnsRsltIdx> list);
	int update(UsptBsnsRsltIdx info);
	int delete(@Param("rsltIdxId") String rsltIdxId
			, @Param("bsnsCd") String bsnsCd);
	List<UsptBsnsRsltIdx> selectList(String bsnsCd);
}
