package aicluster.batch.api.session.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import aicluster.batch.api.session.service.SessionService;
import aicluster.framework.batch.BatchJob;
import aicluster.framework.batch.BatchReturn;

@Component
public class SessionBatchTask {
	@Autowired
	private SessionService service;

    @Scheduled(cron = "0 0 1 * * ?") // 매일 01시 00분
    @BatchJob(jobName = "Expired Session 삭제", loggable = true, checkRunnable = false)
    public BatchReturn saveHolidays() {
//    	return service.removeExpired();
    	return BatchReturn.skip("인증 세션 정보 삭제 배치 SKIP");
    }
}
