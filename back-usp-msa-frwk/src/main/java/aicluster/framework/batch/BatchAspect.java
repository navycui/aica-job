package aicluster.framework.batch;

import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import aicluster.framework.config.EnvConfig;
import aicluster.framework.log.LogUtils;
import aicluster.framework.log.vo.BatchLog;
import bnet.library.util.CoreUtils.exception;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BatchAspect {

//    @Autowired
//    private EnvConfig config;
//
//    @Autowired
//    private LogUtils logUtils;
//
    public static BatchReturn logAround(ProceedingJoinPoint pjp, EnvConfig config, LogUtils logUtils) {

    	MethodSignature sig = (MethodSignature) pjp.getSignature();
        BatchJob job = sig.getMethod().getAnnotation(BatchJob.class);
        String batchName = job.jobName();
        boolean loggable = job.loggable() && logUtils.loggable();
        String batchMethod = pjp.getSignature().getName();

        Date beginDt = new Date();
        long btime = System.currentTimeMillis();
        long etime = 0;

        Object value = null;
        BatchReturn br = null;

//        if (job.checkRunnable() && !isBatchMachine()) {
//        	return BatchReturn.skip("NOT A BATCH MACHINE");
//        }

        BatchLog batchLog = new BatchLog();
        if (loggable) {
	        batchLog.setApiSystemId(config.getSystemId());
	        batchLog.setBatchName(batchName);
	        batchLog.setBatchMethod(batchMethod);
	        batchLog.setBeginDt(beginDt);
        }

        try {
            log.info(String.format("==BATCH-LOG(%s): START", batchName + " > " + batchMethod));

            value = pjp.proceed();
            br = (BatchReturn)value;

            etime = System.currentTimeMillis();
            if (loggable) {
	            batchLog.setElapsedTime(etime - btime);
	            batchLog.setBatchSt(br.getStatus());
	            batchLog.setResultCn(br.getResult());
            }
        } catch (Throwable e) {
        	String msg = exception.getStackTraceString(e);
        	log.error(msg);

            etime = System.currentTimeMillis();
            br = BatchReturn.exception("오류로 작업 중단");

            if (loggable) {
	            batchLog.setElapsedTime(etime - btime);
	            batchLog.setBatchSt(br.getStatus());
	            batchLog.setResultCn(br.getResult());
	            batchLog.setErrorCode(e.getClass().getName());
	            batchLog.setErrorMsg(msg);
            }
        }

        log.info(String.format("==BATCH-LOG(%s): ELAPSED TIME=%d ms, STATUS=%s, RESULT=(%s)", batchName + " > " + batchMethod, (etime-btime), br.getStatus(), br.getResult()));

        if (loggable) {
        	logUtils.saveBatchLog(batchLog);
        }

        return br;
    }

//    private boolean isBatchMachine() {
//    	String serverIp = config.getBatchServerIp();
//    	if (string.isBlank(serverIp)) {
//    		logger.info("=====================================================================================");
//            logger.info("ThenetCoreConfig.batchServerIp is not defined");
//            logger.info("=====================================================================================");
//    		return false;
//    	}
//        List<String> localIPs = system.getLocalIpAddrs();
//        if (!localIPs.contains(serverIp)) {
//            String[] ipArr = new String[localIPs.size()];
//            ipArr = localIPs.toArray(ipArr);
//            logger.info("==========================================================================================================================");
//            logger.info(String.format("THIS SERVER IS NOT A BATCH SERVER(LOCAL IP=[%s], BATCH MACHINE ip=[%s])", StringUtils.join(ipArr, ','), serverIp));
//            logger.info("==========================================================================================================================");
//            return false;
//        }
//        return true;
//    }
}
