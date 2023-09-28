package aicluster.framework.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import aicluster.framework.config.EnvConfig;
import aicluster.framework.log.vo.AccessLog;
import aicluster.framework.log.vo.BatchLog;
import aicluster.framework.log.vo.ErrorLog;
import aicluster.framework.log.vo.LoginLog;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.string;

@Component
public class LogUtils {
    private static final Logger logger = LoggerFactory.getLogger(LogUtils.class);

    @Autowired
    private EnvConfig config;

    private static final String LOGIN_LOG_DIR = "loginLog";
    private static final String ACCESS_LOG_DIR = "accessLog";
    private static final String ERROR_LOG_DIR = "errorLog";
    private static final String BATCH_LOG_DIR = "batchLog";

    public boolean loggable() {
    	return string.isNotBlank(config.getDir().getCoreLog());
    }
    public String getLoginLogDirectory() {
        String dir = getLogDirectory(LOGIN_LOG_DIR);
        if (dir == null) {
            return null;
        }
        return dir + File.separator + "work";
    }

    public String getAccessLogDirectory() {
        String dir = getLogDirectory(ACCESS_LOG_DIR);
        if (dir == null) {
            return null;
        }
        return dir + File.separator + "work";
    }

    public String getErrorLogDirectory() {
        String dir = getLogDirectory(ERROR_LOG_DIR);
        if (dir == null) {
            return null;
        }
        return dir + File.separator + "work";
    }

    public String getBatchLogDirectory() {
        String dir = getLogDirectory(BATCH_LOG_DIR);
        if (dir == null) {
            return null;
        }
        return dir + File.separator + "work";
    }

    public void saveLoginLog(LoginLog log) {
        log.setApiSystemId(config.getSystemId());

        String logDir = makedir(LOGIN_LOG_DIR);
        if (logDir == null) {
            return;
        }

        String filename = makeFilename("login");
        makeLogFile(logDir, filename, log);
    }

    public void saveAccessLog(AccessLog log) {
        log.setApiSystemId(config.getSystemId());
        String logDir = makedir(ACCESS_LOG_DIR);
        if (logDir == null) {
            return;
        }

        // 파일명 생성
        String filename = makeFilename("access");
        makeLogFile(logDir, filename, log);
    }

    public void saveErrorLog(ErrorLog log) {
        log.setApiSystemId(config.getSystemId());
        String logDir = makedir(ERROR_LOG_DIR);
        if (logDir == null) {
            return;
        }

        String filename = makeFilename("error");
        makeLogFile(logDir, filename, log);

    }

    public void saveBatchLog(BatchLog log) {
        log.setApiSystemId(config.getSystemId());
        String logDir = makedir(BATCH_LOG_DIR);
        if (logDir == null) {
            return;
        }

        String filename = makeFilename("batch");
        makeLogFile(logDir, filename, log);
    }

    private <T> void makeLogFile(String logDir, String filename, T log) {
        File src = new File(logDir + File.separator + "temp" + File.separator + filename);
        File dest = new File(logDir + File.separator + "work" + File.separator + filename);
        try (FileOutputStream fos = new FileOutputStream(src); ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            oos.writeObject(log);
        } catch (Throwable e) {
            logger.debug("exception:" + e.getMessage());
            return;
        }
        src.renameTo(dest);
    }

    private String makeFilename(String prefix) {
        return prefix + "-" + CoreUtils.date.format(new Date(), "yyyy.MM.dd-HH.mm.ss.SSS") + "-" + string.getNewId("");
    }

    private String getLogDirectory(String subdir) {
        String rootDir = config.getDir().getCoreLog();
        if (string.isBlank(rootDir)) {
            logger.error("Need to set ThenetCoreConfig.coreLogDir");
            return null;
        }
        if (rootDir.charAt(rootDir.length()-1) == File.separatorChar) {
            rootDir = rootDir.substring(0, rootDir.length()-1);
        }
        return rootDir + File.separator + config.getSystemId() + File.separator + subdir;
    }

    private String makedir(String subdir) {
        String dir = getLogDirectory(subdir);
        if (string.isBlank(dir)) {
            return null;
        }
        File fdir = new File(dir);

        try {
            if (!fdir.exists()) {
                CoreUtils.file.forceMkdir(fdir);
            }
            fdir = new File(dir + File.separator + "temp");
            if (!fdir.exists()) {
            	CoreUtils.file.forceMkdir(fdir);
            }
            fdir = new File(dir + File.separator + "work");
            if (!fdir.exists()) {
            	CoreUtils.file.forceMkdir(fdir);
            }
        } catch (IOException e) {
            logger.error("Fail to make log directory(" + dir + ")", e);
            return null;
        }
        if (!fdir.exists()) {
            return null;
        }
        return dir;
    }

}
