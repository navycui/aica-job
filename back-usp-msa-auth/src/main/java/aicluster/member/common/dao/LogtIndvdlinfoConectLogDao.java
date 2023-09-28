package aicluster.member.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.member.common.entity.LogtIndvdlinfoConectLog;

@Repository
public interface LogtIndvdlinfoConectLogDao {
	void insert(LogtIndvdlinfoConectLog log);
}
