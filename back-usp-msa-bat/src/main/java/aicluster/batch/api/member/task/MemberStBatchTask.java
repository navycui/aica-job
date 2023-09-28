package aicluster.batch.api.member.task;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import aicluster.batch.api.member.service.MemberService;
import aicluster.framework.batch.BatchJob;
import aicluster.framework.batch.BatchReturn;
import bnet.library.util.CoreUtils.date;

@Component
public class MemberStBatchTask {
	@Autowired
	private MemberService service;

    @Scheduled(cron = "0 10 0 * * ?") // 매일 00:10
	//@Scheduled(cron = "0 15 12 * * ?")
    @BatchJob(jobName = "회원상태변경", loggable = true, checkRunnable = false)
    public BatchReturn uptMemberSt() {
    	Date now = new Date();

    	// 불량회원 복권
    	int blackCnt = service.updateBlackMemberToNormal();

    	// 휴면회원 전환 대상자 이메일 발송
    	int dormantEmailCnt = service.sendEmailToDormantMember();

    	// 휴면회원 처리
    	int dormantCnt = service.updateDormantMember();

    	// 탈퇴 7일 경과한 탈퇴회원 삭제
    	int secessionCnt = service.deleteSecessionMember();

    	// 회원현황일별통계 생성
    	Date yesterDt = date.addDays(now, -1);
    	String yesterday = date.format(yesterDt, "yyyyMMdd");
    	int dayStatsCnt = service.createMemberDayStats(yesterday);

    	// 회원현황월별통계 생성
    	String thisMonth = date.format(now, "yyyyMM");
    	int monthStatsCnt = service.createMemberMonthStats(thisMonth);
    	
    	// 관리자 휴면 처리
    	int adminDormantCnt = service.updateDormantAdmin();

    	String fmt = "불량회원 복권: %d, 회원현황일별통계: %d, 회원현황월별통계: %d, 휴면회원전환대상자 이메일발송: %d, 휴면회원처리: %d, 탈퇴회원처리: %d, 관리자 휴면처리: %d";
    	String result = String.format(fmt, blackCnt, dayStatsCnt, monthStatsCnt, dormantEmailCnt, dormantCnt, secessionCnt, adminDormantCnt);
        return BatchReturn.success(result);
    }
}
