package aicluster.common.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.common.common.dto.ElapsedTimeLogSummary;

@Repository
public interface LogtElapseTimeLogDao {
	ElapsedTimeLogSummary selectElapsedTimeLogSummary();
}
