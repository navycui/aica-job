package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptStdrAtchmnflSetup;

@Repository
public interface UsptStdrAtchmnflSetupDao {
	void insert(UsptStdrAtchmnflSetup info);
	int update(UsptStdrAtchmnflSetup info);
	int delete(UsptStdrAtchmnflSetup info);
	UsptStdrAtchmnflSetup select(@Param("stdrBsnsCd") String stdrBsnsCd
								, @Param("atchmnflSetupId") String atchmnflSetupId);
	List<UsptStdrAtchmnflSetup> selectList(String stdrBsnsCd);
}
