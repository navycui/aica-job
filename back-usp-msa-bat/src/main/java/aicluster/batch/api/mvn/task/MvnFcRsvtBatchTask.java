package aicluster.batch.api.mvn.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import aicluster.batch.api.mvn.service.MvnFcRsvtService;
import aicluster.framework.batch.BatchJob;
import aicluster.framework.batch.BatchReturn;

@Component
public class MvnFcRsvtBatchTask {
	@Autowired
	private MvnFcRsvtService service;

	@Scheduled(cron = "19 00 0 * * ?") // 매일 19:00 수행
	@BatchJob(jobName = "시설예약 이용종료", loggable = true, checkRunnable = false)
	public BatchReturn mvnCheckout() {

		int closeCnt = service.updateRsvtClose();;

        return BatchReturn.success(String.format("시설예약 이용종료 처리: %d", closeCnt));
	}
}
