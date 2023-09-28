package aicluster.common.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.common.common.entity.CmmtEmpInfo;

@Repository
public interface CmmtEmpInfoDao {
	CmmtEmpInfo select(String memberId);
}
