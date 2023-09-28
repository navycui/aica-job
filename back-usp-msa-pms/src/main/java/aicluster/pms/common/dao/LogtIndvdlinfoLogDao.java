package aicluster.pms.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.LogtIndvdlinfoLog;

@Repository
public interface LogtIndvdlinfoLogDao {
	void insert(LogtIndvdlinfoLog log);
}
