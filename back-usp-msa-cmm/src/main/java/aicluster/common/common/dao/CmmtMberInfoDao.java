package aicluster.common.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.common.common.entity.CmmtMberInfo;

@Repository
public interface CmmtMberInfoDao {
	CmmtMberInfo select(String memberId);
}
