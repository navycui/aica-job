package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptBsnsAtchmnflSetup;

@Repository
public interface UsptBsnsAtchmnflSetupDao {
	void insert(UsptBsnsAtchmnflSetup info);
	void insertList(List<UsptBsnsAtchmnflSetup> list);
	int update(UsptBsnsAtchmnflSetup info);
	int delete(UsptBsnsAtchmnflSetup info);
	UsptBsnsAtchmnflSetup select(@Param("bsnsCd") String bsnsCd
								, @Param("atchmnflSetupId") String atchmnflSetupId);
	List<UsptBsnsAtchmnflSetup> selectList(String bsnsCd);
}
