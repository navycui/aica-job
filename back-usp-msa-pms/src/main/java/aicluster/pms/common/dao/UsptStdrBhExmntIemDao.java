package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptStdrBhExmntIem;

@Repository
public interface UsptStdrBhExmntIemDao {
	void insert(UsptStdrBhExmntIem info);
	int update(UsptStdrBhExmntIem info);
	int delete(UsptStdrBhExmntIem info);
	int deleteAll(String bhExmntIemId);
	List<UsptStdrBhExmntIem> selectList(String bhExmntIemId);
}
