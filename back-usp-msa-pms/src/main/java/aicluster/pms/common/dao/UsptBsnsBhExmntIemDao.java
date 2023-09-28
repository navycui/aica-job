package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptBsnsBhExmntIem;

@Repository
public interface UsptBsnsBhExmntIemDao {
	void insert(UsptBsnsBhExmntIem info);
	void insertList(List<UsptBsnsBhExmntIem> list);
	int update(UsptBsnsBhExmntIem info);
	int delete(UsptBsnsBhExmntIem info);
	int deleteAll(String bhExmntId);
}
