package aicluster.batch.api.notification.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import aicluster.batch.api.notification.service.NotificationService;
import aicluster.framework.batch.BatchJob;
import aicluster.framework.batch.BatchReturn;

@Component
public class NotificationBatchTask {
	@Autowired
	private NotificationService service;

    @Scheduled(cron = "0 0 9,13,17 * * ?") // 매일 9시, 13시, 17
	//@Scheduled(cron = "0 8 19 * * ?") // 매일 9시, 13시, 17
    @BatchJob(jobName = "알림", loggable = true, checkRunnable = false)
    public BatchReturn notifyError() {
    	service.notifyError();

    	String result = "";
        return BatchReturn.success(result);
    }
}
