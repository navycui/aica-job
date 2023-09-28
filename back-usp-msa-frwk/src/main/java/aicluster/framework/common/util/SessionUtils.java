package aicluster.framework.common.util;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import aicluster.framework.common.dao.CmmtSesionInfoDao;
import aicluster.framework.common.dto.SessionConsentDto;
import aicluster.framework.common.entity.CmmtSesionInfo;
import aicluster.framework.common.util.dto.NiceIdResult;
import aicluster.framework.common.util.dto.PkiResult;
import aicluster.framework.common.util.dto.SessionTermsConsent;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.json;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.CoreUtils.webutils;

@Component("FwSwssionUtils")
public class SessionUtils {

	@Autowired
	private CmmtSesionInfoDao dao;
	@Autowired
	private HttpServletRequest request;

	/**
	 * 만료일시 30분 연장
	 *
	 * @param sessionId
	 */
	public void updateExpiredDt(String sessionId)
	{
		updateExpiredDt(sessionId, 30);
	}

	/**
	 * 만료일시를 입력된 분만큼 연장
	 *
	 * @param sessionId
	 * @param addMin
	 */
	public void updateExpiredDt(String sessionId, int addMin)
	{
		Date now = new Date();
		Date expiredDt = date.addMinutes(now, addMin);
		dao.updateExpiredDt(sessionId, expiredDt);
	}

	/**
	 * 세션 만료
	 *
	 * @param sessionId
	 */
	public void expire(String sessionId) {
		Date now = new Date();
		dao.updateExpiredDt(sessionId, now);
	}

	/**
	 * 약관동의정보 Session 처리
	 */
	public class TermConsentSession
	{
		private final int EXPIRED_MINUTE = 10;  // 만료시간을 10분으로 정의

		/**
		 * 약관동의정보 Session 저장
		 *
		 * @param consents 약관동의정보 목록
		 * @return Session ID
		 */
		public String set(List<SessionConsentDto> consents) {
			String sessionId = string.getNewId("consent-");
			SessionTermsConsent dto = new SessionTermsConsent(webutils.getRemoteIp(request), consents);
			String sessionValue = json.toString(dto);

			Date now = new Date();
			Date expiredDt = date.addMinutes(now, EXPIRED_MINUTE);
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
		 * Session 테이블에 저장된 약관동의정보 조회
		 *
		 * @param sessionId Session ID
		 * @return 약관동의정보 목록
		 */
		public List<SessionConsentDto> get(String sessionId) {
			Date now = new Date();
			CmmtSesionInfo session = dao.select(sessionId);

			if ( session == null || date.getDiffSeconds(now, session.getExpiredDt()) < 0 ) {
				throw new InvalidationException("만료된 약관동의 정보입니다.");
			}

			SessionTermsConsent dto = json.toObject(session.getSessionValue(), SessionTermsConsent.class);
			if ( !string.equals(dto.getUserRemoteIp(), webutils.getRemoteIp(request)) ) {
				throw new InvalidationException("잘못된 접근입니다.");
			}

			return dto.getConsents();
		}
	}

	/**
	 * Nice 휴대폰 본인인증 환경정보 Session 처리
	 */
	public class NiceIdSession {
		private final int EXPIRED_MINUTE = 60;  // 만료시간을 60분으로 정의

		public String set(String siteCode) {
			String sessionId = string.getNewId(siteCode + "-");
			Date now = new Date();
			Date expiredDt = date.addMinutes(now, EXPIRED_MINUTE);
			CmmtSesionInfo session = CmmtSesionInfo.builder()
											.sessionId(sessionId)
											.sessionValue(Boolean.toString(false))
											.createdDt(now)
											.expiredDt(expiredDt)
											.build();
			dao.insert(session);

			return sessionId;
		}

		public boolean check(String sessionId) {
			Date now = new Date();
			CmmtSesionInfo session = dao.select(sessionId);

			if ( session == null || date.getDiffSeconds(now, session.getExpiredDt()) < 0 ) {
				return false;
			}

			Boolean value = string.toBoolean(session.getSessionValue());
			if ( value == null || value ) {
				return false;
			}

			session.setSessionValue(Boolean.toString(true));
			dao.update(session);
			return true;
		}
	}

	/**
	 * 휴대폰 본인인증 인증결과 정보 Session 처리
	 */
	public class MobileCertSession
	{
		private final int EXPIRED_MINUTE = 60;  // 만료시간을 60분으로 정의

		/**
		 * 휴대폰본인인증 결과를 세션에 저장한다.
		 * @param result NiceId 결과
		 * @return Session ID
		 */
		public String set(NiceIdResult result) {
			String sessionId = string.getNewId("niceid-");
			Date now = new Date();
			Date expiredDt = date.addMinutes(now, EXPIRED_MINUTE);
			String sessionValue = json.toString(result);
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
		 * Session에 저장된 휴대폰본인인증 결과(NiceIdResult)를 조회한다.
		 *
		 * @param sessionId 세션ID
		 * @return 휴대폰본인인증 결과
		 */
		public NiceIdResult get(String sessionId) {
			Date now = new Date();
			CmmtSesionInfo session = dao.select(sessionId);
			if (session == null) {
				throw new InvalidationException("휴대폰 본인인증 결과가 없습니다.");
			}

			if (now.compareTo(session.getExpiredDt()) > 0) {
				throw new InvalidationException("휴대폰 본인인증 결과 저장기간이 만료되었습니다.");
			}

			NiceIdResult result = session.getSessionJsonValue(NiceIdResult.class);
			if (result == null) {
				throw new InvalidationException("휴대폰 본인인증 결과가 없습니다.");
			}

			return result;
		}
	}

	/**
	 * 공동인증서 인증결과 정보 Session 처리
	 */
	public class PkiCertSession
	{
		private final int EXPIRED_MINUTE = 60;  // 만료시간을 60분으로 정의

		/**
		 * 공동인증 결과를 세션에 저장한다.
		 * @param result 공동인증서 인증결과
		 * @return Session ID
		 */
		public String set(PkiResult result) {
			String sessionId = string.getNewId("pkiceid-");
			Date now = new Date();
			Date expiredDt = date.addMinutes(now, EXPIRED_MINUTE);
			String sessionValue = json.toString(result);
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
		 * Session에 저장된 공동인증서 인증결과(PkiResult)를 조회한다.
		 *
		 * @param sessionId 세션ID
		 * @return 공동인증서 인증결과
		 */
		public PkiResult get(String sessionId) {
			Date now = new Date();
			CmmtSesionInfo session = dao.select(sessionId);
			if (session == null) {
				throw new InvalidationException("공동인증서 인증결과가 없습니다.");
			}

			if (now.compareTo(session.getExpiredDt()) > 0) {
				throw new InvalidationException("공동인증서 인증결과 저장기간이 만료되었습니다.");
			}

			PkiResult result = session.getSessionJsonValue(PkiResult.class);
			if (result == null) {
				throw new InvalidationException("공동인증서 인증결과가 없습니다.");
			}

			return result;
		}

		/**
		 * Session에 저장된 공동인증서 인증결과(PkiResult) 값을 검증하고 인증결과를 가져온다.
		 *
		 * @param sessionId
		 * @return 공동인증서 인증결과
		 */
		public PkiResult check(String sessionId)
		{
			PkiResult result = get(sessionId);

	        if ( string.isBlank(result.getIdentifyData()) ) {
	        	throw new InvalidationException("정상적인 공동인증이 이뤄지지 않았습니다.\n공동인증서 인증을 재수행하세요.");
	        }
	        if ( string.isBlank(result.getBizrno()) ) {
	        	throw new InvalidationException("사업자등록번호가 확인되지 않습니다.\n공동인증서 인증을 재수행하세요.");
	        }

	        return result;
		}
	}

	public final TermConsentSession termsConsentSession = new TermConsentSession();
	public final NiceIdSession niceIdSession = new NiceIdSession();
	public final MobileCertSession mobileCertSession = new MobileCertSession();
	public final PkiCertSession pkiCertSession = new PkiCertSession();
}
