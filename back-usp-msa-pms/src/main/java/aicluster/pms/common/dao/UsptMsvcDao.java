package aicluster.pms.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptMsvc;

@Repository
public interface UsptMsvcDao {
	int save(UsptMsvc info);
	UsptMsvc select(String memberId);
}
