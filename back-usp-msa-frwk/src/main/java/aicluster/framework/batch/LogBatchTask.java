package aicluster.framework.batch;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import aicluster.framework.common.service.LogService;
import aicluster.framework.log.LogUtils;
import aicluster.framework.log.vo.AccessLog;
import aicluster.framework.log.vo.BatchLog;
import aicluster.framework.log.vo.ErrorLog;
import aicluster.framework.log.vo.LoginLog;
import bnet.library.util.CoreUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LogBatchTask {
    @Autowired
    private LogUtils logUtils;

	@Autowired
	private LogService logService;

    @Scheduled(cron = "0 */10 * * * ?") // 10분 간격으로 실행
    @BatchJob(jobName = "로그저장", loggable = false, checkRunnable = false)
    public BatchReturn writeLog() {
        String workDir = logUtils.getLoginLogDirectory();
        int loginCnt = saveLog(workDir, LoginLog.class);

        workDir = logUtils.getAccessLogDirectory();
        int accessCnt = saveLog(workDir, AccessLog.class);

        workDir = logUtils.getErrorLogDirectory();
        int errorCnt = saveLog(workDir, ErrorLog.class);

        workDir = logUtils.getBatchLogDirectory();
        int batchCnt = saveLog(workDir, BatchLog.class);

        return BatchReturn.success(String.format("LoginLog=%d, AccessLog=%d, ErrorLog=%d, BatchLog=%d",
                loginCnt, accessCnt, errorCnt, batchCnt));
    }

    synchronized private <T> int saveLog(String workDir, Class<T> cls) {
        if (CoreUtils.string.isBlank(workDir)) {
            return 0;
        }
        File dir = new File(workDir);
        File[] files = dir.listFiles();
        if (files == null) {
            return 0;
        }
        List<File> list = new ArrayList<>();
        for (File file: files) {
            if (file.isFile()) {
                list.add(file);
            }
        }
        if (list.size() == 0) {
            return 0;
        }
        Collections.sort(list, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        // READ
        List<T> logs = new ArrayList<>();
        T log = null;
        for (File file: list) {
            log = readObject(file, cls);
            if (log != null) {
                logs.add(log);
            }
        }

        if (logs.size() > 0) {
        	if (cls == LoginLog.class) {
        		LoginLog[] loginLogs = new LoginLog[logs.size()];
				for(int i = 0; i < logs.size(); i++) {
					LoginLog aLog = (LoginLog)logs.get(i);
					loginLogs[i] = aLog;
				}
				logService.login(loginLogs);
        	}
        	else if (cls == AccessLog.class) {
        		AccessLog[] accessLogs = new AccessLog[logs.size()];
        		for(int i = 0; i < logs.size(); i++) {
        			AccessLog aLog = (AccessLog)logs.get(i);
        			accessLogs[i] = aLog;
				}
				logService.access(accessLogs);
        	}
        	else if (cls == ErrorLog.class) {
        		ErrorLog[] errorLogs = new ErrorLog[logs.size()];
        		for(int i = 0; i < logs.size(); i++) {
        			ErrorLog aLog = (ErrorLog)logs.get(i);
        			errorLogs[i] = aLog;
				}
				logService.error(errorLogs);
        	}
        	else if (cls == BatchLog.class) {
        		BatchLog[] batchLogs = new BatchLog[logs.size()];
        		for(int i = 0; i < logs.size(); i++) {
        			BatchLog aLog = (BatchLog)logs.get(i);
        			batchLogs[i] = aLog;
				}
				logService.batch(batchLogs);
        	}
        }

        // DELETE
        for (File file: list) {
            file.delete();
        }

        return list.size();
    }

    @SuppressWarnings("unchecked")
    private <T> T readObject(File file, Class<T> cls) {
        T obj = null;
        try (
        	FileInputStream fis = new FileInputStream(file);
        	ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            obj = (T)ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("exception:" + e.getMessage());
        }
        return obj;
    }

}
