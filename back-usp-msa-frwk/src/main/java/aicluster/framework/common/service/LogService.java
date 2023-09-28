package aicluster.framework.common.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.dao.FwLogDao;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.LogtConectLog;
import aicluster.framework.common.entity.LogtBatchLog;
import aicluster.framework.common.entity.LogtDeUnitConnectMberLog;
import aicluster.framework.common.entity.LogtElapseTimeLog;
import aicluster.framework.common.entity.LogtErrorLog;
import aicluster.framework.common.entity.LogtLoginLog;
import aicluster.framework.log.vo.AccessLog;
import aicluster.framework.log.vo.BatchLog;
import aicluster.framework.log.vo.ErrorLog;
import aicluster.framework.log.vo.LoginLog;
import aicluster.framework.security.SecurityUtils;
import bnet.library.util.CoreUtils;

@Service("loggerLogService")
public class LogService {

	private static final int MAX_SIZE = 100;

    @Autowired
    private FwLogDao logDao;

    public void login(LoginLog[] logs) {
        List<LogtLoginLog> list = new ArrayList<>();
        LogtLoginLog dbLog = null;
        for (LoginLog log: logs) {
            dbLog = new LogtLoginLog();
            CoreUtils.property.copyProperties(dbLog, log);
            dbLog.setLogId(CoreUtils.string.getNewId("llog-"));
            list.add(dbLog);
            if (list.size() >= MAX_SIZE) {
            	logDao.insertLogtLoginLogs(list);
            	list.clear();
            }
        }
        if (list.size() > 0) {
        	logDao.insertLogtLoginLogs(list);
        }
    }

    public void access(AccessLog[] logs) {
        List<LogtConectLog> list = new ArrayList<>();
        LogtConectLog dbLog = null;
        for (AccessLog log: logs) {
            dbLog = new LogtConectLog();
            CoreUtils.property.copyProperties(dbLog, log);
            dbLog.setLogId(CoreUtils.string.getNewId("alog-"));
            if (CoreUtils.string.isEmpty(log.getLoginId())) {
                continue;
            }
            list.add(dbLog);
            if (list.size() >= MAX_SIZE) {
            	logDao.insertLogtAccessLogs(list);
            	list.clear();
            }
        }
        if (list.size() > 0) {
        	logDao.insertLogtAccessLogs(list);
        }
    }

    public void error(ErrorLog[] logs) {
        if (logs == null || logs.length == 0) {
            return;
        }
        List<LogtErrorLog> list = new ArrayList<>();
        LogtErrorLog dbLog = null;

        for (ErrorLog log: logs) {
            dbLog = new LogtErrorLog();
            CoreUtils.property.copyProperties(dbLog, log);
            dbLog.setLogId(CoreUtils.string.getNewId("elog-"));
            list.add(dbLog);

            if (list.size() >= MAX_SIZE) {
            	logDao.insertLogtErrorLogs(list);
            	list.clear();
            }
        }
        if (list.size() > 0) {
        	logDao.insertLogtErrorLogs(list);
        }

        //TODO : (유영민) 알림 기능 추가 필요
    }

	public void batch(BatchLog[] logs) {
		if (logs == null || logs.length == 0) {
            return;
        }
        List<LogtBatchLog> list = new ArrayList<>();
        LogtBatchLog dbLog = null;

        for (BatchLog log: logs) {
            dbLog = new LogtBatchLog();
            CoreUtils.property.copyProperties(dbLog, log);
            dbLog.setLogId(CoreUtils.string.getNewId("elog-"));
            list.add(dbLog);

            if (list.size() >= MAX_SIZE) {
            	logDao.insertLogtBatchLogs(list);
            	list.clear();
            }
        }
        if (list.size() > 0) {
        	logDao.insertLogtBatchLogs(list);
        }
	}

	public void addElapsedTime(String systemId, String uri, long elapsedTime) {
		Date now = new Date();
		LogtElapseTimeLog logtElapsedTime = LogtElapseTimeLog.builder()
				.logId(CoreUtils.string.getNewId("log-"))
				.logDt(now)
				.url(uri)
				.elapsedTime(elapsedTime)
				.apiSystemId(systemId)
				.build();
		Date removeTime = CoreUtils.date.addMonths(now, -3);
		logDao.deleteLogtElapsedTimeLog(removeTime);
		logDao.insertLogtElapsedTimeLog(logtElapsedTime);
	}

	public void addDayMember() {
		BnMember member = SecurityUtils.getCurrentMember();
		if (member == null) {
			return;
		}

		Date now = new Date();
		String ymd = CoreUtils.date.format(now, "yyyyMMdd");

		LogtDeUnitConnectMberLog dayMember = logDao.selectLogtDayMemberLog(ymd, member.getMemberId());
		if (dayMember != null) {
			return;
		}
		Date removeTime = CoreUtils.date.addMonths(now, -3);
		String removeYmd = CoreUtils.date.format(removeTime, "yyyyMMdd");
		logDao.deleteLogtDayMemberLog(removeYmd);

		dayMember = LogtDeUnitConnectMberLog.builder()
				.ymd(ymd)
				.memberId(member.getMemberId())
				.build();
		logDao.insertLogtDayMemberLog(dayMember);
	}
}
