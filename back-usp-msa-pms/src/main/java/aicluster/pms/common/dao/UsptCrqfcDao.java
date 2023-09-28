package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptCrqfc;

@Repository
public interface UsptCrqfcDao {
	void insert(UsptCrqfc info);
	int update(UsptCrqfc info);
	int delete(UsptCrqfc info);
	List<UsptCrqfc> selectList(String memberId);
}
