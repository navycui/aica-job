package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptStdrRsltIdx;

@Repository
public interface UsptStdrRsltIdxDao {
	void insert(UsptStdrRsltIdx info);
	int update(UsptStdrRsltIdx info);
	int delete(@Param("rsltIdxId") String rsltIdxId
			, @Param("stdrBsnsCd") String stdrBsnsCd);
	List<UsptStdrRsltIdx> selectList(String stdrBsnsCd);
}
