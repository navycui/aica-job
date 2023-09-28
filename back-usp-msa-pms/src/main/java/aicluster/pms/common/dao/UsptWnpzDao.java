package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptWnpz;

@Repository
public interface UsptWnpzDao {
	void insert(UsptWnpz info);
	int update(UsptWnpz info);
	int delete(UsptWnpz info);
	List<UsptWnpz> selectList(String memberId);
}
