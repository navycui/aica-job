package aicluster.pms.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.CmmtEnt;

@Repository
public interface CmmtEntDao {
	CmmtEnt select(String memberId);

}
