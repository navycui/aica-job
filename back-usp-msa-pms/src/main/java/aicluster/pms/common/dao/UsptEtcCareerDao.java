package aicluster.pms.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptEtcCareer;

@Repository
public interface UsptEtcCareerDao {
	void insert(UsptEtcCareer info);
	int update(UsptEtcCareer info);
	int delete(UsptEtcCareer info);
	List<UsptEtcCareer> selectList(String memberId);
}
