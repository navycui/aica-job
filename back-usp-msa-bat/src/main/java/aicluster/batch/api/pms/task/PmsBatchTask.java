package aicluster.batch.api.pms.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import aicluster.batch.api.pms.service.PmsService;
import aicluster.framework.batch.BatchJob;
import aicluster.framework.batch.BatchReturn;

@Component
public class PmsBatchTask {
	@Autowired
	private PmsService pmsService;

	/**
	 * 공고 게시 처리
	 * @return
	 */
	@Scheduled(cron = "5 0 0 * * ?") // 매일 0시 0분 5초
	@BatchJob(jobName = "공고 게시 처리", loggable = true, checkRunnable = false)
	public BatchReturn updateNtce() {
		return pmsService.updateNtce();
	}


	/**
	 * 공고 접수마감 처리
	 * @return
	 */
	@Scheduled(cron = "3 0 0/1 * * *") // 1시간마다
	@BatchJob(jobName = "공고 접수마감 처리", loggable = true, checkRunnable = false)
	public BatchReturn updateRceptClosing() {
		return pmsService.updateRceptClosing();
	}


	/**
	 * LMS 카테고리 동기화
	 * 공통코드 테이블에 저장
	 * @return
	 */
	@Scheduled(cron = "1 0 0 * * ?") // 매일 0시 0분 1초
	@BatchJob(jobName = "LMS 카테고리 동기화", loggable = true, checkRunnable = false)
	public BatchReturn updateLmsCategory() {
		return pmsService.updateLmsCategory();
	}
}
