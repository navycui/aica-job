package aicluster.batch.api.member.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.batch.common.dao.CmmtEmpInfoDao;
import aicluster.batch.common.dao.CmmtEntrprsInfoDao;
import aicluster.batch.common.dao.CmmtMberInfoDao;
import aicluster.batch.common.dao.CmmtMberDeUnitStatsDao;
import aicluster.batch.common.dao.CmmtMberInfoHistDao;
import aicluster.batch.common.dao.CmmtMberMtUnitStatsDao;
import aicluster.batch.common.dto.MemberStatsItem;
import aicluster.batch.common.entity.CmmtMberInfo;
import aicluster.batch.common.entity.CmmtMberDeUnitStats;
import aicluster.batch.common.entity.CmmtMberInfoHist;
import aicluster.batch.common.util.CodeExt;
import aicluster.framework.notification.EmailUtils;
import aicluster.framework.notification.dto.EmailSendParam;
import aicluster.framework.notification.nhn.email.EmailReceiver;
import aicluster.framework.security.Code;
import bnet.library.exception.LoggableException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MemberService {

	public static final String[] WEEKDAY_NM = {"일","월","화","수","목","금","토"};
	@Autowired
	private CmmtMberInfoDao cmmtMemberDao;
	@Autowired
	private CmmtMberDeUnitStatsDao cmmtMemberDayStatsDao;
	@Autowired
	private CmmtMberMtUnitStatsDao cmmtMemberMonthStatsDayDao;
	@Autowired
	private CmmtMberInfoHistDao cmmtMemberHistDao;
	@Autowired
	private CmmtEntrprsInfoDao cmmtEntDao;
	@Autowired
	private CmmtEmpInfoDao cmmtEmpInfoDao;
	@Autowired
	private EmailUtils emailUtils;

	/**
	 * 불량회원 복원
	 *
	 * @return
	 */
	public int updateBlackMemberToNormal() {

		// 불량 회원 복원 대상 조회
		List<CmmtMberInfo> list = cmmtMemberDao.selectList_blackMemberToNormal();

		/*
		 * 불량회원 복원
		 */
		List<CmmtMberInfoHist> histList = new ArrayList<>();
		CmmtMberInfoHist hist = null;
		Date now = new Date();
		String workCn = "회원상태가 변경되었습니다.(불량회원 > 정상)";
		for (CmmtMberInfo cmmtMember : list) {
			// 복원
			cmmtMember.setMemberSt(Code.memberSt.정상);
			cmmtMember.setMemberStDt(now);
			cmmtMember.setBlackListBeginDay(null);
			cmmtMember.setBlackListEndDay(null);
			cmmtMember.setBlackListReason(null);
			cmmtMemberDao.update(cmmtMember);

			// 이력 생성
			hist = CmmtMberInfoHist.builder()
					.histId(string.getNewId(CodeExt.prefixId.이력ID))
					.histDt(now)
					.workTypeNm("불량회원복권")
					.memberId(cmmtMember.getMemberId())
					.workerId("BATCH")
					.workCn(workCn)
					.build();

			histList.add(hist);
		}

		if (histList.size() > 0) {
			cmmtMemberHistDao.insertList(histList);
		}
		log.info("불량회원 복원: " + list.size());
		return list.size();
	}

	/**
	 * 일별 회원현황 통계 생성
	 * @return
	 */
	public int createMemberDayStats(String ymd) {
		// 날짜 확인
		Date now = new Date();
		Date dt = date.parseDateSilently(ymd, "yyyyMMdd");
		if (dt == null) {
			dt = date.addDays(now, -1);
		}

		// 요일 구하기
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int weekday = cal.get(Calendar.DAY_OF_WEEK);
		String weekDayNm = WEEKDAY_NM[weekday - 1];

		List<CmmtMberDeUnitStats> all = new ArrayList<>();

		// 누적 회원수
		List<MemberStatsItem> list = cmmtMemberDayStatsDao.selectTotalMbrCnt(ymd);
		for (MemberStatsItem item : list) {
			CmmtMberDeUnitStats mds = new CmmtMberDeUnitStats();
			mds.setStatsYmd(ymd);
			mds.setMemberType(item.getMemberType());
			mds.setWeekDayNm(weekDayNm);
			mds.setTotalMbrCnt(item.getCnt());
			mds.setCreatedDt(now);
			all.add(mds);
		}

		// 당일 회원가입자수
		list = cmmtMemberDayStatsDao.selectJoinMbrCnt(ymd);
		for (MemberStatsItem item : list) {
			CmmtMberDeUnitStats mds = findMemberDayStats(all, ymd, item.getMemberType());
			if (mds == null) {
				mds = new CmmtMberDeUnitStats();
				mds.setStatsYmd(ymd);
				mds.setMemberType(item.getMemberType());
				mds.setCreatedDt(now);
				all.add(mds);
			}
			mds.setJoinMbrCnt(item.getCnt());
		}

		// 당일 탈퇴자수
		list = cmmtMemberDayStatsDao.selectSecessionMbrCnt(ymd);
		for (MemberStatsItem item : list) {
			CmmtMberDeUnitStats mds = findMemberDayStats(all, ymd, item.getMemberType());
			if (mds == null) {
				mds = new CmmtMberDeUnitStats();
				mds.setStatsYmd(ymd);
				mds.setMemberType(item.getMemberType());
				mds.setCreatedDt(now);
				all.add(mds);
			}
			mds.setSecessionMbrCnt(item.getCnt());
		}

		// 당일 휴면회원수
		list = cmmtMemberDayStatsDao.selectDormantMbrCnt(ymd);
		for (MemberStatsItem item : list) {
			CmmtMberDeUnitStats mds = findMemberDayStats(all, ymd, item.getMemberType());
			if (mds == null) {
				mds = new CmmtMberDeUnitStats();
				mds.setStatsYmd(ymd);
				mds.setMemberType(item.getMemberType());
				mds.setCreatedDt(now);
				all.add(mds);
			}
			mds.setDormantMbrCnt(item.getCnt());
		}

		// 당일 불량회원수
		list = cmmtMemberDayStatsDao.selectBlackMbrCnt(ymd);
		for (MemberStatsItem item : list) {
			CmmtMberDeUnitStats mds = findMemberDayStats(all, ymd, item.getMemberType());
			if (mds == null) {
				mds = new CmmtMberDeUnitStats();
				mds.setStatsYmd(ymd);
				mds.setMemberType(item.getMemberType());
				mds.setCreatedDt(now);
				all.add(mds);
			}
			mds.setBlackMbrCnt(item.getCnt());
		}

		cmmtMemberDayStatsDao.upsert(all);
		return all.size();
	}

	private CmmtMberDeUnitStats findMemberDayStats(List<CmmtMberDeUnitStats> all, String ymd, String memberType) {
		for (CmmtMberDeUnitStats item : all) {
			if (string.equals(ymd, item.getStatsYmd()) && string.equals(memberType, item.getMemberType())) {
				return item;
			}
		}
		return null;
	}

	/**
	 * 월별 회원현황 통계 생성
	 * @return
	 */
	public int createMemberMonthStats(String ym) {
		int cnt = cmmtMemberMonthStatsDayDao.upsert(ym);
		return cnt;
	}

	/**
	 * 휴면회원 이메일 발송
	 * 30일전, 7일전 사용자 이메일 발송
	 * @return
	 */
	public int sendEmailToDormantMember() {
		/*
		 * 휴면 30일 전 사용자 조회
		 */
		Date now = new Date();
		Date dt30 = date.addDays(now, -(365 - 30));
		String day30 = date.format(dt30, "yyyyMMdd");
		List<CmmtMberInfo> list30 = cmmtMemberDao.selectList_lastLoginDt(day30);

		/*
		 * 휴면 7일전 사용자 조회
		 */
		Date dt7 = date.addDays(now, -(365 - 7));
		String day7 = date.format(dt7, "yyyyMMdd");
		List<CmmtMberInfo> list7 = cmmtMemberDao.selectList_lastLoginDt(day7);

		/*
		 * 메일 발송
		 */
		String emailFilePath = "/form/email/email-dormant30.html";
		String emailCn = CoreUtils.file.readFileString(emailFilePath);
		if (emailCn == null) {
			throw new LoggableException(emailFilePath + " not found");
		}
		int sendCnt = sendEmail(list30, "(AICA)휴면회원전환 30일 전입니다", emailCn);

		emailFilePath = "/form/email/email-dormant7.html";
		emailCn = CoreUtils.file.readFileString(emailFilePath);
		if (emailCn == null) {
			throw new LoggableException(emailFilePath + " not found");
		}

		sendCnt += sendEmail(list7, "(AICA)휴면회원전환 7일 전입니다", emailCn);

		return sendCnt;
	}

	/**
	 * 이메일 발송
	 *
	 * @param receiverList	수신자 목록
	 * @param title	이메일 제목
	 * @param emailCn	이메일 내용
	 * @return	발송건수
	 */
	private int sendEmail(List<CmmtMberInfo> receiverList, String title, String emailCn) {
		if (receiverList == null || receiverList.size() == 0) {
			return 0;
		}
		List<EmailReceiver> list = new ArrayList<>();
		Map<String, String> templateParameter = null;
		for (CmmtMberInfo cmmtMember : receiverList) {
			if (string.isBlank(cmmtMember.getEmail())) {
				continue;
			}
			templateParameter = new HashMap<>();
			templateParameter.put("memberNm", cmmtMember.getMemberNm());
			EmailReceiver er = new EmailReceiver(cmmtMember.getEmail(), cmmtMember.getMemberNm(), templateParameter);
			list.add(er);
		}
		EmailSendParam param = new EmailSendParam();
		param.setContentHtml(true);
		param.setEmailCn(emailCn);
		param.setRecipientList(list);;
		param.setTitle(title);

		emailUtils.send(param);

		return list.size();
	}

	/**
	 * 휴면회원 처리
	 * 마지막 로그인한 지 365일이 지난 회원의 상태를 휴면회원으로 수정
	 *
	 * @return
	 */
	public int updateDormantMember() {
		Date now = new Date();
		Date dt365 = date.addDays(now, -365);
		String day365 = date.format(dt365, "yyyyMMdd");
		int cnt = cmmtMemberDao.updateToDormant(day365);
		return cnt;
	}

	/**
	 * 회원탈퇴 7일 경과한 회원 데이터 삭제
	 * (탈퇴 완료 이메일 발송)
	 *
	 * @return 탈퇴 처리 건수
	 */
	public int deleteSecessionMember()
	{
		Date now = new Date();

		// CMMT_MEMBER에서 탈퇴한 지 7일 지난 데이터 조회
		List<CmmtMberInfo> secesMemberList = cmmtMemberDao.selectList_memberSt(Code.memberSt.탈퇴, 7);

		// CMMT_MEMBER과 CMMT_ENT에서 삭제
		List<CmmtMberInfoHist> histList = new ArrayList<>();
		for (CmmtMberInfo member : secesMemberList) {
			// 회원정보 삭제(월별 통계를 도출하기 위해 로직 주석 처리)
			//cmmtMemberDao.delete(member.getMemberId());

			// 회원정보 Null update(월별 통계를 도출하기 위해 row는 남겨놓는다.)
			member.setLoginId(member.getMemberId());
			member.setEncPasswd("secession");
			member.setPasswdInit(false);
			member.setMemberNm("탈퇴자");
			member.setNickname("탈퇴자");
			member.setGender(null);
			member.setCi(null);
			member.setEncEmail(null);
			member.setEncBirthday(null);
			member.setEncMobileNo(null);
			member.setChargerNm(null);
			member.setCeoNm(null);
			member.setBizrno(null);
			member.setJurirno(null);
			member.setInstr(false);
			member.setMarketingReception(null);
			member.setRefreshToken(null);
			member.setKakaoToken(null);
			member.setNaverToken(null);
			member.setGoogleToken(null);
			member.setUpdatedDt(now);
			member.setUpdaterId("BATCH");

			cmmtMemberDao.update(member);

			// 기업정보 삭제
			cmmtEntDao.delete(member.getMemberId());

			// 이력정보 생성
			CmmtMberInfoHist hist = CmmtMberInfoHist.builder()
										.histId(string.getNewId(CodeExt.prefixId.이력ID))
										.histDt(now)
										.workTypeNm("회원탈퇴")
										.memberId(member.getMemberId())
										.workerId("BATCH")
										.workCn("탈퇴 7일이 경과하여 회원정보가 삭제되었습니다.")
										.build();
			histList.add(hist);
		}

		// 회원이력 생성
		if (histList.size() > 0) {
			cmmtMemberHistDao.insertList(histList);
		}

		int rstCnt = secesMemberList.size();

//		/*
//		 * 탈퇴 완료 메일 발송
//		 */
//		String emailFilePath = "/form/email/email-secession.html";
//		String emailCn = CoreUtils.file.readFileString(emailFilePath);
//		if (emailCn == null) {
//			throw new LoggableException(emailFilePath + " not found");
//		}
//		int sendCnt = sendEmail(secesMemberList, "(AICA)탈퇴 처리가 완료되었습니다.", emailCn);

		return rstCnt;
	}

	public int updateDormantAdmin() {
		Date now = new Date();
		Date dt365 = date.addDays(now, -365);
		String day365 = date.format(dt365, "yyyyMMdd");
		int cnt = cmmtEmpInfoDao.updateToDormantAdmin(day365);
		return cnt;
	}

//	/**
//	 * 회원탈퇴 7일 경과한 데이터 백업
//	 *
//	 * @return
//	 */
//	public int backupSecessionMember() {
//		//TODO : (유영민) 회원탈퇴 7일 경과한 데이터 백업(의사결정 필요)
//
//		// CMMT_MEMBER에서 탈퇴한 지 7일 지난 데이터 조회
//		List<CmmtMember> secesList = cmmtMemberDao.selectList_memberSt(CodeExt.memberSt.탈퇴, 7);
//
//		// CMMT_MEMBER_SECESSION에 복사
//
//		// CMMT_MEMBER에서 삭제
//		for (CmmtMember member : secesList) {
//			cmmtMemberDao.delete(member.getMemberId());
//		}
//
//		return secesList.size();
//	}
//
//	/**
//	 * 탈퇴한 지 5년 지난 Backup Data 삭제
//	 *
//	 * @return
//	 */
//	public int deleteSecessionBackupMember() {
//		//TODO : (유영민) 탈퇴한 지 5년 지난 Backup data 삭제
//		return 0;
//	}
}
