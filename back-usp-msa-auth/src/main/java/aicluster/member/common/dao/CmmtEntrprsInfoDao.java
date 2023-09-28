package aicluster.member.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.member.common.entity.CmmtEntrprsInfo;

@Repository
public interface CmmtEntrprsInfoDao {

	CmmtEntrprsInfo select(String memberId);

	void insert(CmmtEntrprsInfo cmmtEnt);

	void update(CmmtEntrprsInfo cmmtEnt);

	void delete(String memberId);



}
