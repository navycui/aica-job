package aicluster.batch.api.holiday.task;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import aicluster.batch.api.holiday.service.HolidayService;
import aicluster.framework.batch.BatchJob;
import aicluster.framework.batch.BatchReturn;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;

@Component
public class HolidayBatchTask {
	@Autowired
	private HolidayService service;

    @Scheduled(cron = "0 5 0 * * ?") // 매일 00시 05분
    @BatchJob(jobName = "휴일정보 가져오기", loggable = true, checkRunnable = false)
    public BatchReturn saveHolidays() {
    	Date now = new Date();
    	String strYear = date.format(now, "yyyy");
    	String strMonth = date.format(now, "MM");
    	int beginYear = string.toInt(strYear);
    	int beginMonth = string.toInt(strMonth);
    	int cnt = 12;
    	int totalItems = service.updateHolidays(beginYear, beginMonth, cnt);
    	String result = String.format("시작월: %04d년 %02d월, 개월수: %d 개월, 휴일건수: %d건", beginYear, beginMonth, cnt, totalItems);
        return BatchReturn.success(result);
    }
}
