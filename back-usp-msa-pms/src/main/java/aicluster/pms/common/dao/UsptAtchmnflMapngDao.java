package aicluster.pms.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptAtchmnflMapng;

@Repository
public interface UsptAtchmnflMapngDao {
	UsptAtchmnflMapng select(String registId);
	void insert(UsptAtchmnflMapng info);
	int update(UsptAtchmnflMapng info);
	int updateLinkUrl(UsptAtchmnflMapng info);
}
