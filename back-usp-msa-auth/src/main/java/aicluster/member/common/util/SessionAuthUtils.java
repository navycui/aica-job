package aicluster.member.common.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import aicluster.framework.common.dao.CmmtSesionInfoDao;
import aicluster.framework.common.entity.CmmtSesionInfo;
import aicluster.framework.common.util.InvalidStatus;
import aicluster.framework.notification.EmailUtils;
import aicluster.framework.notification.SmsUtils;
import aicluster.framework.notification.dto.EmailSendParam;
import aicluster.framework.notification.dto.SmsCertParam;
import aicluster.framework.notification.nhn.email.EmailResult;
import aicluster.framework.notification.nhn.sms.SmsResult;
import aicluster.member.common.util.dto.SessionAccountCertDto;
import aicluster.member.common.util.dto.SessionEmailCertDto;
import aicluster.member.common.util.dto.SessionFindPwdDto;
import aicluster.member.common.util.dto.SessionIdSetForBzmnDto;
import aicluster.member.common.util.dto.SessionIdSetForJoinDto;
import aicluster.member.common.util.dto.SessionMobileNoCertDto;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.LoggableException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.json;
import bnet.library.util.CoreUtils.string;

@Component
public class SessionAuthUtils {

	@Autowired
	private CmmtSesionInfoDao dao;
    @Autowired
    private EmailUtils emailUtils;
	@Autowired
	private SmsUtils smsUtils;

	/**
	 * 비밀번호 확인용 Session
	 */
	public class PasswdCheck {
		/**
		 * 비밀번호 확인 session 값 저장
		 * @param memberId
		 * @return sessionId
		 */
		public String set(String memberId) {
			String sessionId = string.getNewId(CodeExt.prefix.비밀번호확인세션ID);
			Date now = new Date();
			Date expiredDt = date.addMinutes(now, 30);
			CmmtSesionInfo session = CmmtSesionInfo.builder()
					.sessionId(sessionId)
					.sessionValue(memberId)
					.createdDt(now)
					.expiredDt(expiredDt)
					.build();
			dao.insert(session);

			return sessionId;
		}

		/**
		 * 비밀번호 확인 session 값 구하기
		 * @param sessionId
		 * @return memberId
		 */
		public String get(String sessionId) {
			CmmtSesionInfo session = dao.select(sessionId);
			if (session == null) {
				throw new InvalidationException(InvalidStatus.STATUS_PASSWD_CHECK_NEEDED, "비밀번호 확인을 먼저 하십시오.");
			}
			return session.getSessionValue();
		}
	}

	/**
	 * 휴대폰 SMS 인증번호 인증
	 */
	public class MobileSmsNoCert {
		/**
		 * 인증번호 SMS 발송
		 *
		 * @param mobileNo	휴대폰번호
		 * @param certNo	인증번호
		 */
		public void sendSms(String mobileNo, String certNo) {
			mobileNo = string.getNumberOnly(mobileNo);
			if (string.isBlank(mobileNo)) {
				throw new InvalidationException("휴대폰번호를 입력하세요.");
			}

			/*
			 * 인증번호 발송
			 */
			String smsCn = CoreUtils.file.readFileString("/form/sms/phone-cert.txt");
			if (string.isBlank(smsCn)) {
				throw new LoggableException("SMS발송본문 파일을 읽을 수 없습니다.");
			}
			SmsCertParam sap = new SmsCertParam();
			sap.setSmsCn(smsCn);
			sap.addRecipient(mobileNo, "certNo", certNo);
			SmsResult sr = smsUtils.sendCert(sap);
			if (sr.getSuccessCnt() == 0) {
				throw new InvalidationException("SMS 인증번호 발송에 실패하였습니다. 잠시 후에 다시 이용해 주십시오.");
			}
		}

		/**
		 * 휴대폰 SMS 인증번호 저장
		 *
		 * @param mobileNo 휴대폰번호
		 * @param certNo 인증번호
		 * @return sessionId
		 */
		public String set(String mobileNo, String certNo) {
			mobileNo = string.getNumberOnly(mobileNo);

			String sessionId = string.getNewId(CodeExt.prefix.SMS인증세션ID);
			SessionMobileNoCertDto dto = SessionMobileNoCertDto.builder()
					.mobileNo(mobileNo)
					.certNo(certNo)
					.checked(false)
					.build();
			String sessionValue = json.toString(dto);

			Date now = new Date();
			Date expiredDt = date.addMinutes(now, 10);
			CmmtSesionInfo session = CmmtSesionInfo.builder()
					.sessionId(sessionId)
					.sessionValue(sessionValue)
					.createdDt(now)
					.expiredDt(expiredDt)
					.build();
			dao.insert(session);

			return sessionId;
		}

		/**
		 * 휴대폰 SMS 인증번호 확인
		 *
		 * @param sessionId 세션ID
		 * @param certNo 인증번호
		 * @return 일치여부
		 */
		public boolean check(String sessionId, String certNo) {
			CmmtSesionInfo session = dao.select(sessionId);
			if (session == null) {
				throw new InvalidationException("인증정보가 없습니다. 인증번호를 다시 발송하십시오.");
			}
			if (string.isBlank(session.getSessionValue())) {
				throw new InvalidationException("인증번호가 없습니다.");
			}
			SessionMobileNoCertDto dto = json.toObject(session.getSessionValue(), SessionMobileNoCertDto.class);
			if (dto == null) {
				throw new InvalidationException("올바른 사용법이 아닙니다.");
			}

			boolean same = string.equals(dto.getCertNo(), certNo);
			if (same) {
				dto.setChecked(true);
				String sessionValue = json.toString(dto);
				Date now = new Date();
				Date expiredDt = date.addMinutes(now, 30);
				session.setSessionValue(sessionValue);
				session.setExpiredDt(expiredDt);
				dao.update(session);
			}
			return same;
		}

		/**
		 * 휴대폰번호 조회
		 * @param mobileNo 휴대폰번호
		 * @return sessionId
		 */
		public String getMobileNo(String sessionId) {
			CmmtSesionInfo session = dao.select(sessionId);
			if (session == null) {
				throw new InvalidationException("인증정보가 없습니다. 인증번호를 다시 발송하십시오.");
			}
			if (string.isBlank(session.getSessionValue())) {
				throw new InvalidationException("인증번호가 없습니다.");
			}
			SessionMobileNoCertDto dto = json.toObject(session.getSessionValue(), SessionMobileNoCertDto.class);
			if (dto == null) {
				throw new InvalidationException("올바른 사용법이 아닙니다.");
			}
			if (!dto.isChecked()) {
				throw new InvalidationException("인증번호 확인을 하지 않았습니다.");
			}
			return dto.getMobileNo();
		}
	}

	/**
	 * 이메일 인증번호
	 *
	 */
	public class EmailCert {
		/**
		 * 인증번호 메일 발송
		 *
		 * @param mberNm	회원
		 * @param email		이메일
		 * @param certNo	인증번호
		 */
		public void sendMail(String mberNm, String email, String certNo) {
			/*
			 * 인증번호 발송
			 */
			String emailFilePath = "/form/email/email-cert.html";
			String emailCn = CoreUtils.file.readFileString(emailFilePath);
			if (emailCn == null) {
				throw new LoggableException("인증메일 발송본문 파일을 읽을 수 없습니다.");
			}
			EmailSendParam esp = new EmailSendParam();
			esp.setContentHtml(true);
			esp.setEmailCn(emailCn);
			esp.setTitle("[인공지능산업융합사업단] 인증번호");

			Map<String, String> templateParam = new HashMap<>();
			templateParam.put("certNo", certNo);
			esp.addRecipient(email, mberNm, templateParam);

			EmailResult er = emailUtils.send(esp);
			if (er.getSuccessCnt() == 0) {
				throw new LoggableException("이메일 인증번호 발송에 실패하였습니다. 잠시 후에 다시 이용해 주십시오.");
			}
		}

		/**
		 * 이메일 인증번호 저장
		 *
		 * @param email 이메일
		 * @param certNo 인증번호
		 * @return sessionId
		 */
		public String set(String email, String certNo) {
			String sessionId = string.getNewId(CodeExt.prefix.이메일인증세션ID);
			SessionEmailCertDto dto = SessionEmailCertDto.builder()
					.email(email)
					.certNo(certNo)
					.checked(false)
					.build();
			String sessionValue = json.toString(dto);

			Date now = new Date();
			Date expiredDt = date.addMinutes(now, 10);
			CmmtSesionInfo session = CmmtSesionInfo.builder()
					.sessionId(sessionId)
					.sessionValue(sessionValue)
					.createdDt(now)
					.expiredDt(expiredDt)
					.build();
			dao.insert(session);

			return sessionId;
		}

		/**
		 * 이메일 인증번호 확인
		 * @param sessionId 세션ID
		 * @param certNo 인증번호
		 * @return 일치여부
		 */
		public boolean check(String sessionId, String certNo) {
			CmmtSesionInfo session = dao.select(sessionId);
			if (session == null) {
				throw new InvalidationException("인증정보가 없습니다. 인증번호를 다시 발송하십시오.");
			}
			if (string.isBlank(session.getSessionValue())) {
				throw new InvalidationException("인증번호가 없습니다.");
			}
			SessionEmailCertDto dto = json.toObject(session.getSessionValue(), SessionEmailCertDto.class);
			if (dto == null) {
				throw new InvalidationException("올바른 사용법이 아닙니다.");
			}
			boolean same = string.equals(dto.getCertNo(), certNo);
			if (same) {
				dto.setChecked(true);
				String sessionValue = json.toString(dto);
				Date now = new Date();
				Date expiredDt = date.addMinutes(now, 30);
				session.setSessionValue(sessionValue);
				session.setExpiredDt(expiredDt);
				dao.update(session);
			}
			return same;
		}

		/**
		 * 이메일 조회
		 *
		 * @param sessionId
		 * @return
		 */
		public String getEmail(String sessionId) {
			CmmtSesionInfo session = dao.select(sessionId);
			if (session == null) {
				throw new InvalidationException("인증정보가 없습니다. 인증번호를 다시 발송하십시오.");
			}
			if (string.isBlank(session.getSessionValue())) {
				throw new InvalidationException("인증번호가 없습니다.");
			}
			SessionEmailCertDto dto = json.toObject(session.getSessionValue(), SessionEmailCertDto.class);
			if (dto == null) {
				throw new InvalidationException("올바른 사용법이 아닙니다.");
			}
			if (!dto.isChecked()) {
				throw new InvalidationException("인증번호 확인을 하지 않았습니다.");
			}
			return dto.getEmail();
		}
	}

	public class FindPassword {
		public String set(String memberId, String mobileNo, String email) {
			if (string.isBlank(memberId)) {
				throw new InvalidationException("회원ID를 입력하세요.");
			}
			if (string.isBlank(mobileNo)) {
				throw new InvalidationException("휴대폰번호를 입력하세요.");
			}
			if (string.isBlank(email)) {
				throw new InvalidationException("이메일을 입력하세요.");
			}
			String sessionId = string.getNewId(CodeExt.prefix.비밀번호찾기세션ID);
			mobileNo = string.getNumberOnly(mobileNo);
			email = string.deleteWhitespace(email);
			SessionFindPwdDto dto = SessionFindPwdDto.builder()
					.memberId(memberId)
					.mobileNo(mobileNo)
					.email(email)
					.certNo(null)
					.checked(false)
					.build();
			String sessionValue = json.toString(dto);
			Date now = new Date();
			Date expired = date.addMinutes(now, 60);

			CmmtSesionInfo session = CmmtSesionInfo.builder()
					.sessionId(sessionId)
					.sessionValue(sessionValue)
					.createdDt(now)
					.expiredDt(expired)
					.build();
			dao.insert(session);
			return sessionId;
		}

		public String getMobileNo(String sessionId) {
			CmmtSesionInfo session = dao.select(sessionId);
			if (session == null) {
				throw new InvalidationException("비밀번호 재설정 인증정보가 없습니다.");
			}

			SessionFindPwdDto dto = json.toObject(session.getSessionValue(), SessionFindPwdDto.class);
			if (dto == null) {
				throw new InvalidationException("올바른 사용법이 아닙니다.");
			}

			return dto.getMobileNo();
		}

		public String getEmail(String sessionId) {
			CmmtSesionInfo session = dao.select(sessionId);
			if (session == null) {
				throw new InvalidationException("비밀번호 재설정 인증정보가 없습니다.");
			}

			SessionFindPwdDto dto = json.toObject(session.getSessionValue(), SessionFindPwdDto.class);
			if (dto == null) {
				throw new InvalidationException("올바른 사용법이 아닙니다.");
			}

			return dto.getEmail();
		}

		public void setCertNo(String sessionId, String certNo) {
			CmmtSesionInfo session = dao.select(sessionId);
			if (session == null) {
				throw new InvalidationException("비밀번호 재설정 인증정보가 없습니다.");
			}

			SessionFindPwdDto dto = json.toObject(session.getSessionValue(), SessionFindPwdDto.class);
			if (dto == null) {
				throw new InvalidationException("올바른 사용법이 아닙니다.");
			}

			Date now = new Date();
			Date expiredDt = date.addMinutes(now, 10);
			dto.setCertNo(certNo);
			dto.setCertExpiredDt(expiredDt);
			dto.setChecked(false);

			String sessionValue = json.toString(dto);
			expiredDt = date.addMinutes(now, 30);
			session.setSessionValue(sessionValue);
			session.setExpiredDt(expiredDt);
			dao.update(session);
		}

		public boolean check(String sessionId, String certNo) {
			Date now = new Date();
			CmmtSesionInfo session = dao.select(sessionId);
			if (session == null) {
				throw new InvalidationException("인증정보가 없습니다. 인증번호를 다시 발송하십시오.");
			}
			if (string.isBlank(session.getSessionValue())) {
				throw new InvalidationException("인증번호가 없습니다.");
			}
			SessionFindPwdDto dto = json.toObject(session.getSessionValue(), SessionFindPwdDto.class);
			if (dto == null) {
				throw new InvalidationException("올바른 사용법이 아닙니다.");
			}

			if (now.compareTo(dto.getCertExpiredDt()) > 0) {
				throw new InvalidationException("인증기간이 만료되었습니다. 다시 인증하십시오.");
			}

			boolean same = string.equals(dto.getCertNo(), certNo);
			if (same) {
				dto.setChecked(true);
				String sessionValue = json.toString(dto);
				Date expiredDt = date.addMinutes(now, 30);
				session.setSessionValue(sessionValue);
				session.setExpiredDt(expiredDt);
				dao.update(session);
			}
			return same;
		}

		public String getMemberId(String sessionId) {
			CmmtSesionInfo session = dao.select(sessionId);
			if (session == null) {
				throw new InvalidationException("인증정보가 없습니다. 인증번호를 다시 발송하십시오.");
			}
			if (string.isBlank(session.getSessionValue())) {
				throw new InvalidationException("인증번호가 없습니다.");
			}
			SessionFindPwdDto dto = json.toObject(session.getSessionValue(), SessionFindPwdDto.class);
			if (dto == null) {
				throw new InvalidationException("올바른 사용법이 아닙니다.");
			}
			if (!dto.isChecked()) {
				throw new InvalidationException("인증번호 확인을 먼저 하십시오.");
			}
			return dto.getMemberId();
		}
	}

	/**
	 * 회원가입 Session 처리
	 */
	public class JoinSessId {
		public String set(SessionIdSetForJoinDto sesionIdSet) {
			String sessionId = string.getNewId(CodeExt.prefix.가입세선ID);
			Date now = new Date();
			Date expiredDt = date.addMinutes(now, 30);
			String sessionValue = json.toString(sesionIdSet);
			CmmtSesionInfo session = CmmtSesionInfo.builder()
					.sessionId(sessionId)
					.sessionValue(sessionValue)
					.createdDt(now)
					.expiredDt(expiredDt)
					.build();
			dao.insert(session);
			return sessionId;
		}

		public SessionIdSetForJoinDto get(String sessionId) {
			Date now = new Date();
			CmmtSesionInfo session = dao.select(sessionId);
			if (session == null) {
				throw new InvalidationException("회원가입 세션정보 결과가 없습니다.");
			}

			if (now.compareTo(session.getExpiredDt()) > 0) {
				throw new InvalidationException("회원가입 세션정보 저장기간이 만료되었습니다.");
			}

			SessionIdSetForJoinDto result = session.getSessionJsonValue(SessionIdSetForJoinDto.class);
			if (result == null) {
				throw new InvalidationException("회원가입 세션정보 결과가 없습니다.");
			}

			return result;
		}
	}

	/**
	 * 사업자전환 Session 처리
	 */
	public class BzmnSessId {
		public String set(SessionIdSetForBzmnDto sesionIdSet) {
			String sessionId = string.getNewId(CodeExt.prefix.사업자전환세션ID);
			Date now = new Date();
			Date expiredDt = date.addMinutes(now, 30);
			String sessionValue = json.toString(sesionIdSet);
			CmmtSesionInfo session = CmmtSesionInfo.builder()
					.sessionId(sessionId)
					.sessionValue(sessionValue)
					.createdDt(now)
					.expiredDt(expiredDt)
					.build();
			dao.insert(session);
			return sessionId;
		}

		public SessionIdSetForBzmnDto get(String sessionId) {
			Date now = new Date();
			CmmtSesionInfo session = dao.select(sessionId);
			if (session == null) {
				throw new InvalidationException("사업자전환 세션정보 결과가 없습니다.");
			}

			if (now.compareTo(session.getExpiredDt()) > 0) {
				throw new InvalidationException("사업자전환 세션정보 저장기간이 만료되었습니다.");
			}

			SessionIdSetForBzmnDto result = session.getSessionJsonValue(SessionIdSetForBzmnDto.class);
			if (result == null) {
				throw new InvalidationException("사업자전환 세션정보 결과가 없습니다.");
			}

			return result;
		}
	}

	/**
	 * 계정 인증결과 정보 Session 처리
	 */
	public class AccountSessId {
		/**
		 * 계정 인증 결과를 세션에 저장한다.
		 *
		 * @param result NiceId 결과
		 * @return Session ID
		 */
		public String set(SessionAccountCertDto dto) {
			String sessionId = string.getNewId(CodeExt.prefix.계정인증ID);
			Date now = new Date();
			Date expiredDt = date.addMinutes(now, 5);  // 5분 제한
			String sessionValue = json.toString(dto);
			CmmtSesionInfo session = CmmtSesionInfo.builder()
					.sessionId(sessionId)
					.sessionValue(sessionValue)
					.createdDt(now)
					.expiredDt(expiredDt)
					.build();
			dao.insert(session);
			return sessionId;
		}

		/**
		 * Session에 저장된 계정 인증 결과(SessionAccountCertDto)를 조회한다.
		 *
		 * @param sessionId 세션ID
		 * @return 계정인증 결과
		 */
		public SessionAccountCertDto get(String sessionId) {
			Date now = new Date();
			CmmtSesionInfo session = dao.select(sessionId);
			if (session == null) {
				throw new InvalidationException("계정 인증 결과가 없습니다.");
			}

			if (now.compareTo(session.getExpiredDt()) > 0) {
				throw new InvalidationException("계정 인증 결과 저장기간이 만료되었습니다.");
			}

			SessionAccountCertDto dto = session.getSessionJsonValue(SessionAccountCertDto.class);
			if (dto == null) {
				throw new InvalidationException("계정 인증 결과가 없습니다.");
			}

			return dto;
		}
	}

	public final PasswdCheck passwdCheck = new PasswdCheck();
	public final MobileSmsNoCert mobileSmsNoCert = new MobileSmsNoCert();
	public final EmailCert emailCert = new EmailCert();
	public final FindPassword findPassword = new FindPassword();
	public final JoinSessId joinSessId = new JoinSessId();
	public final AccountSessId accountSessId = new AccountSessId();
	public final BzmnSessId bzmnSessId = new BzmnSessId();
}
