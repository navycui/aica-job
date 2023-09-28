package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptJobCareer;

@Repository
public interface UsptJobCareerDao {
	void insert(UsptJobCareer info);
	int update(UsptJobCareer info);
	int delete(UsptJobCareer info);
	List<UsptJobCareer> selectList(String memberId);
}
