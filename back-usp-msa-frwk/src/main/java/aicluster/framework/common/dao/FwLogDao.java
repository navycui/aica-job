package aicluster.framework.common.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.framework.common.entity.LogtConectLog;
import aicluster.framework.common.entity.LogtBatchLog;
import aicluster.framework.common.entity.LogtDeUnitConnectMberLog;
import aicluster.framework.common.entity.LogtElapseTimeLog;
import aicluster.framework.common.entity.LogtErrorLog;
import aicluster.framework.common.entity.LogtIndvdlinfoConectLog;
import aicluster.framework.common.entity.LogtIndvdlinfoDwldLog;
import aicluster.framework.common.entity.LogtIndvdlinfoDwldTrget;
import aicluster.framework.common.entity.LogtLoginLog;

@Repository
public interface FwLogDao {

    void insertLogtLoginLogs(@Param("logs") List<LogtLoginLog> logs);

    void insertLogtAccessLogs(@Param("logs") List<LogtConectLog> logs);

    void insertLogtErrorLogs(@Param("logs") List<LogtErrorLog> logs);

    void insertLogtBatchLogs(@Param("logs") List<LogtBatchLog> logs);

	void insertLogtElapsedTimeLog(LogtElapseTimeLog logtElapsedTimLog);

	void deleteLogtElapsedTimeLog(Date removeTime);

	LogtDeUnitConnectMberLog selectLogtDayMemberLog(
			@Param("ymd") String ymd,
			@Param("memberId") String memberId);

	void deleteLogtDayMemberLog(String removeYmd);

	void insertLogtDayMemberLog(LogtDeUnitConnectMberLog dayMember);

	void insertLogtIndvdlinfoDwldLog(LogtIndvdlinfoDwldLog logtIndvdlinfoDownLog);

	void insertLogtIndvdlinfoDwldTrget(List<LogtIndvdlinfoDwldTrget> list);

	void insertLogtIndvdlinfoConectLog(LogtIndvdlinfoConectLog log);
}
