package aicluster.batch.api.mvn.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import aicluster.batch.api.mvn.service.MvnCheckoutService;
import aicluster.framework.batch.BatchJob;
import aicluster.framework.batch.BatchReturn;

@Component
public class MvnCheckoutBatchTask {

	@Autowired
	private MvnCheckoutService service;

	@Scheduled(cron = "0 30 0 * * ?") // 매일 00:30 수행
	@BatchJob(jobName = "입주업체 퇴실처리", loggable = true, checkRunnable = false)
	public BatchReturn mvnCheckout() {

		int checkoutCnt = service.updateCheckout();

        return BatchReturn.success(String.format("입주업체퇴실처리: %d", checkoutCnt));
	}
}
