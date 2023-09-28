package aicluster.member.api.login.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import aicluster.framework.common.dao.FwUsptEvlMfcmmDao;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.EvlMfcmm;
import aicluster.framework.common.entity.TokenDto;
import aicluster.framework.common.util.InvalidStatus;
import aicluster.framework.config.EnvConfig;
import aicluster.framework.log.LogUtils;
import aicluster.framework.log.vo.LoginLog;
import aicluster.framework.notification.EmailUtils;
import aicluster.framework.notification.dto.EmailSendParam;
import aicluster.framework.notification.nhn.email.EmailResult;
import aicluster.framework.security.JwtUtils;
import aicluster.framework.security.SecurityUtils;
import aicluster.framework.security.TokenProvider;
import aicluster.member.api.login.config.GoogleConfig;
import aicluster.member.api.login.config.KakaoConfig;
import aicluster.member.api.login.dto.EvlLoginParam;
import aicluster.member.api.login.dto.GoogleLoginResponse;
import aicluster.member.api.login.dto.KakaoLoginParam;
import aicluster.member.api.login.dto.KakaoLoginResponse;
import aicluster.member.api.login.dto.KakaoTokenResponse;
import aicluster.member.api.login.dto.LoginParam;
import aicluster.member.api.login.dto.LoginResult;
import aicluster.member.api.login.dto.NaverLoginResponse;
import aicluster.member.api.login.dto.SnsLoginParam;
import aicluster.member.api.login.dto.SsoParam;
import aicluster.member.common.dao.CmmtEmpInfoDao;
import aicluster.member.common.dao.CmmtLoginAtptDao;
import aicluster.member.common.dao.CmmtMberInfoDao;
import aicluster.member.common.dao.UsptExpertDao;
import aicluster.member.common.entity.CmmtEmpInfo;
import aicluster.member.common.entity.CmmtMberInfo;
import aicluster.member.common.entity.UsptExpert;
import aicluster.member.common.util.CodeExt;
import bnet.library.exception.CommunicationException;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.LoggableException;
import bnet.library.exception.UnauthorizedException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.antpath;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.password;
import bnet.library.util.CoreUtils.property;
import bnet.library.util.CoreUtils.string;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoginService {
	private static final int PASSWD_CHANGE_TERM = 60;

	@Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;
	@Autowired
	private EnvConfig envConfig;
	@Autowired
    private CmmtMberInfoDao memberDao;
	@Autowired
	private CmmtEmpInfoDao insiderDao;
	@Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private LogUtils logUtils;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private KakaoConfig kakaoConfig;
    @Autowired
    private GoogleConfig googleConfig;
    @Autowired
    private UsptExpertDao usptExpertDao;
    @Autowired
    private FwUsptEvlMfcmmDao usptEvlMfcmmDao;
    @Autowired
    private CmmtLoginAtptDao cmmtLoginTryDao;
    @Autowired
    private EmailUtils emailUtils;

	private void saveLoginLog_member(CmmtMberInfo member, String userIp) {
		LoginLog log = new LoginLog();
		Date now = new Date();

		log.setAge(CoreUtils.date.getAge(member.getBirthday()));
		log.setApiSystemId(envConfig.getSystemId());
		log.setDeptNm(null);
		log.setGender(member.getGender());
		log.setLogDt(now);
		log.setLoginId(member.getLoginId());
		log.setMemberId(member.getMemberId());
		log.setMemberIp(userIp);
		log.setMemberNm(member.getMemberNm());
		log.setMemberType(member.getMemberType());
		logUtils.saveLoginLog(log);
	}

	private void saveLoginLog_evaluator(BnMember evaluator, String userIp) {
		LoginLog log = new LoginLog();
		Date now = new Date();

		log.setAge(CoreUtils.date.getAge(evaluator.getBirthday()));
		log.setApiSystemId(envConfig.getSystemId());
		log.setDeptNm(evaluator.getDeptNm());
		log.setGender(evaluator.getGender());
		log.setLogDt(now);
		log.setLoginId(evaluator.getLoginId());
		log.setMemberId(evaluator.getMemberId());
		log.setMemberIp(userIp);
		log.setMemberNm(evaluator.getMemberNm());
		log.setMemberType(evaluator.getMemberType());
		logUtils.saveLoginLog(log);
	}

	private void saveLoginLog_insider(CmmtEmpInfo insider, String userIp) {
		LoginLog log = new LoginLog();
		Date now = new Date();

		log.setAge(null);
		log.setApiSystemId(envConfig.getSystemId());
		log.setDeptNm(insider.getDeptNm());
		log.setGender(insider.getGender());
		log.setLogDt(now);
		log.setLoginId(insider.getLoginId());
		log.setMemberId(insider.getMemberId());
		log.setMemberIp(userIp);
		log.setMemberNm(insider.getMemberNm());
		log.setMemberType(CodeExt.memberTypeExt.내부사용자);
		logUtils.saveLoginLog(log);
	}

    public LoginResult login_sso(SsoParam param, String userIp) {
    	if (param == null || string.isBlank(param.getLoginId()) || string.isBlank(param.getKey()) || string.isBlank(param.getResultCode())) {
    		throw new InvalidationException("입력이 없습니다.");
    	}

    	if (!string.equals(param.getResultCode(), "000000")) {
    		throw new InvalidationException("입력값이 올바르지 않습니다.");
    	}
    	String loginId = aes256.decrypt(param.getLoginId(), param.getKey());
    	String passwd = password.getRandomPassword();
    	String encPasswd = password.encode(passwd);

    	log.info("sso-loginId:" + loginId);
        CmmtMberInfo member = memberDao.selectByLoginId(loginId);
        if (member == null) {
        	throw new InvalidationException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        if (string.equals(member.getMemberSt(), CodeExt.memberSt.잠김)) {
        	String lockErrMsg = "비밀번호 연속 오류로 계정이 잠겼습니다.";
        	if (CodeExt.memberTypeExt.isIndividual(member.getMemberType())) {
            	throw new InvalidationException(InvalidStatus.STATUS_개인회원_계정잠김, lockErrMsg);
        	}
        	else {
            	throw new InvalidationException(InvalidStatus.STATUS_사업자회원_계정잠김, lockErrMsg);
        	}
        }

        if (string.equals(member.getMemberSt(), CodeExt.memberSt.휴면)) {
        	String dormantErrMsg = "오랫동안 로그인하지 않아 휴면 처리된 계정입니다.";
        	if (CodeExt.memberTypeExt.isIndividual(member.getMemberType())) {
            	throw new InvalidationException(InvalidStatus.STATUS_개인회원_계정휴면, dormantErrMsg);
        	}
        	else {
            	throw new InvalidationException(InvalidStatus.STATUS_사업자회원_계정휴면, dormantErrMsg);
        	}
        }

        if (string.equals(member.getMemberSt(), CodeExt.memberSt.탈퇴)) {
        	throw new InvalidationException("최근 7일이내에 탈퇴한 계정입니다.\n서비스 이용하시려면 회원가입을 통해 본인 인증을 수행하세요.");
        }

        if (!string.equals(member.getMemberSt(), CodeExt.memberSt.정상)) {
        	throw new InvalidationException("로그인 계정에 대한 상태를 관리자에게 문의하세요.");
        }

        // 로그인시도 오래된 데이터 삭제
        cmmtLoginTryDao.deleteOld(member.getMemberId(), userIp);

        // Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("sso:" + loginId + ":" + encPasswd, passwd);

        // 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        // authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // RefreshToken 저장
        Date now = new Date();
        member.setRefreshToken(tokenDto.getRefreshToken());
        member.setLastLoginDt(now);
        memberDao.update(member);

        // 로그인 로그 생성
        saveLoginLog_member(member, userIp);

        /*
         * 로그인시도 이력 삭제
         */
        cmmtLoginTryDao.delete(member.getMemberId(), userIp);

        // 비밀번호 변경 시점인가?
        int diffDay = date.getDiffDays(member.getPasswdDt(), now);
        boolean changePasswd = diffDay >= PASSWD_CHANGE_TERM;

        LoginResult result = new LoginResult();
        property.copyProperties(result, tokenDto);
        result.setChangePasswd(changePasswd);
        result.setInitPasswd(member.getPasswdInit());
        result.setDbid(member.getLoginId());
        result.setDbpw(member.getEncPasswd());

        return result;
    }

    public LoginResult login_member(LoginParam param, String userIp) {
    	if (param == null || string.isBlank(param.getLoginId()) || string.isBlank(param.getPasswd())) {
    		throw new InvalidationException("아이디와 비밀번호를 모두 입력하세요.");
    	}

        CmmtMberInfo member = memberDao.selectByLoginId(param.getLoginId());
        if (member == null) {
        	throw new InvalidationException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        if (string.equals(member.getMemberSt(), CodeExt.memberSt.잠김)) {
        	String lockErrMsg = "비밀번호 연속 오류로 계정이 잠겼습니다.";
        	if (CodeExt.memberTypeExt.isIndividual(member.getMemberType())) {
            	throw new InvalidationException(InvalidStatus.STATUS_개인회원_계정잠김, lockErrMsg);
        	}
        	else {
            	throw new InvalidationException(InvalidStatus.STATUS_사업자회원_계정잠김, lockErrMsg);
        	}
        }

        if (string.equals(member.getMemberSt(), CodeExt.memberSt.휴면)) {
        	String dormantErrMsg = "오랫동안 로그인하지 않아 휴면 처리된 계정입니다.";
        	if (CodeExt.memberTypeExt.isIndividual(member.getMemberType())) {
            	throw new InvalidationException(InvalidStatus.STATUS_개인회원_계정휴면, dormantErrMsg);
        	}
        	else {
            	throw new InvalidationException(InvalidStatus.STATUS_사업자회원_계정휴면, dormantErrMsg);
        	}
        }

        if (string.equals(member.getMemberSt(), CodeExt.memberSt.탈퇴)) {
        	throw new InvalidationException("최근 7일이내에 탈퇴한 계정입니다.\n서비스 이용하시려면 회원가입을 통해 본인 인증을 수행하세요.");
        }

        if (!string.equals(member.getMemberSt(), CodeExt.memberSt.정상)) {
        	throw new InvalidationException("로그인 계정에 대한 상태를 관리자에게 문의하세요.");
        }

        // 로그인시도 오래된 데이터 삭제
        cmmtLoginTryDao.deleteOld(member.getMemberId(), userIp);

        // Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("member:" + param.getLoginId(), param.getPasswd());

        // 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        // authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // RefreshToken 저장
        Date now = new Date();
        member.setRefreshToken(tokenDto.getRefreshToken());
        member.setLastLoginDt(now);
        memberDao.update(member);

        // 로그인 로그 생성
        saveLoginLog_member(member, userIp);

        /*
         * 로그인시도 이력 삭제
         */
        cmmtLoginTryDao.delete(member.getMemberId(), userIp);

        // 비밀번호 변경 시점인가?
        int diffDay = date.getDiffDays(member.getPasswdDt(), now);
        boolean changePasswd = diffDay >= PASSWD_CHANGE_TERM;

        LoginResult result = new LoginResult();
        property.copyProperties(result, tokenDto);
        result.setChangePasswd(changePasswd);
        result.setInitPasswd(member.getPasswdInit());
        result.setDbid(member.getLoginId());
        result.setDbpw(member.getEncPasswd());

        return result;
    }

	public LoginResult login_memberNaver(SnsLoginParam param, String userIp) {
		if (param == null || string.isBlank(param.getAccessToken())) {
			throw new InvalidationException("입력값이 올바르지 않습니다.");
		}

		/*
		 * Access token으로 NAVER 사용자 정보 조회
		 */
		Unirest.config().reset();
		Unirest.config().socketTimeout(1000 * 60 * 3);

		String header = "Bearer " + param.getAccessToken();
		String url = CodeExt.snsUrl.NAVER_ME_URL;
		HttpResponse<NaverLoginResponse> res = Unirest.get(url)
				.header("Authorization", header)
				.asObject(NaverLoginResponse.class);

		if (res.getStatus() != 200) {
			log.error(res.getBody().toString());
			NaverLoginResponse nres = res.getBody();
			String msg = String.format("\"Naver와 통신 중에 오류가 발생하여 작업을 중단하였습니다.(code=%s, message=%s)", nres.getResultcode(), nres.getMessage());
			throw new CommunicationException(msg);
		}

		NaverLoginResponse nres = res.getBody();
		if (!string.equals(nres.getResultcode(), "00")) {
			String msg = String.format("\"Naver와 통신 중에 오류가 발생하여 작업을 중단하였습니다.(code=%s, message=%s)", nres.getResultcode(), nres.getMessage());
			throw new CommunicationException(msg);
		}

		/*
		 * 로그인 처리
		 */
        // Login ID/PW 를 기반으로 AuthenticationToken 생성
		CmmtMberInfo member = memberDao.selectByNaverToken(nres.getResponse().getId());
        if (member == null) {
        	throw new InvalidationException("Naver 계정 연동을 하지 않으셨습니다. 계정 연동을 먼저 하신 후 사용하세요.");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("sns:" + member.getLoginId(), member.getLoginId());

        // 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        // authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // RefreshToken 저장
        Date now = new Date();
        member.setRefreshToken(tokenDto.getRefreshToken());
        member.setLastLoginDt(now);
        memberDao.update(member);

        // 로그인 로그 생성
        saveLoginLog_member(member, userIp);

        // 비밀번호 변경 시점인가?
        int diffDay = date.getDiffDays(member.getPasswdDt(), now);
        boolean changePasswd = diffDay >= PASSWD_CHANGE_TERM;

        LoginResult result = new LoginResult();
        property.copyProperties(result, tokenDto);
        result.setChangePasswd(changePasswd);
        result.setInitPasswd(member.getPasswdInit());
        result.setDbid(member.getLoginId());
        result.setDbpw(member.getEncPasswd());

        return result;
	}

	public LoginResult login_memberGoogle(SnsLoginParam param, String userIp) {
		if (param == null || string.isBlank(param.getAccessToken())) {
			throw new InvalidationException("입력값이 올바르지 않습니다.");
		}

		/*
		 * Access token으로 Google 사용자 정보 조회
		 */
		Unirest.config().reset();
		Unirest.config().socketTimeout(1000 * 60 * 3);

		String url = CodeExt.snsUrl.GOOGLE_ME_URL;
		//personFields:'birthdays', key:'AIzaSyDRmtoYrF3xrtgHk92JDNhO98k5xmXPvkM', 'access_token': access_token
		HttpResponse<GoogleLoginResponse> res = Unirest.get(url)
				.queryString("personFields", "birthdays")
				.queryString("key", googleConfig.getAppKey())
				.queryString("access_token", param.getAccessToken())
				.asObject(GoogleLoginResponse.class);

		if (res.getStatus() != 200) {
			GoogleLoginResponse gr = res.getBody();
			log.error(gr.toString());
			String msg = String.format("\"Google과 통신 중에 오류가 발생하여 작업을 중단하였습니다.(code=%d, message=%s)", gr.getCode(), gr.getMessage());
			throw new CommunicationException(msg);
		}

		GoogleLoginResponse gr = res.getBody();
		String googleId = gr.getPersonId();

		/*
		 * 로그인 처리
		 */

        // Login ID/PW 를 기반으로 AuthenticationToken 생성
		CmmtMberInfo member = memberDao.selectByGoogleToken(googleId);
        if (member == null) {
        	throw new InvalidationException("Google 계정 연동을 하지 않으셨습니다. 계정 연동을 먼저 하신 후 사용하세요.");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("sns:" + member.getLoginId(), member.getLoginId());

        // 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        // authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // RefreshToken 저장
        Date now = new Date();
        member.setRefreshToken(tokenDto.getRefreshToken());
        member.setLastLoginDt(now);
        memberDao.update(member);

        // 로그인 로그 생성
        saveLoginLog_member(member, userIp);

        // 비밀번호 변경 시점인가?
        int diffDay = date.getDiffDays(member.getPasswdDt(), now);
        boolean changePasswd = diffDay >= PASSWD_CHANGE_TERM;

        LoginResult result = new LoginResult();
        property.copyProperties(result, tokenDto);
        result.setChangePasswd(changePasswd);
        result.setInitPasswd(member.getPasswdInit());
        result.setDbid(member.getLoginId());
        result.setDbpw(member.getEncPasswd());

        return result;
	}

	public KakaoLoginResponse getKakaoMemberByAccessToken(String accessToken) {
		Unirest.config().reset();
		Unirest.config().socketTimeout(1000 * 60 * 3);

		String header = "Bearer " + accessToken;
		String url = CodeExt.snsUrl.KAKAO_ME_URL;
		HttpResponse<KakaoLoginResponse> res = Unirest.post(url)
				.header("Authorization", header)
				.asObject(KakaoLoginResponse.class);

		if (res.getStatus() != 200) {
			log.error(res.getBody().toString());
			KakaoLoginResponse nres = res.getBody();
			String msg = String.format("카카오와 통신 중에 오류가 발생하여 작업을 중단하였습니다.(code=%d, message=%s)", nres.getCode(), nres.getMsg());
			throw new CommunicationException(msg);
		}

		KakaoLoginResponse nres = res.getBody();
		log.info("kakao info ===\n" + nres);
		return nres;
	}

	public KakaoLoginResponse getKakaoMemberByCode(String code, String uri) {
		Unirest.config().reset();
		Unirest.config().socketTimeout(1000 * 60 * 3);

		String url = CodeExt.snsUrl.KAKAO_OAUTH_URL;
		HttpResponse<KakaoTokenResponse> res = Unirest.post(url)
				.field("grant_type", "authorization_code")
				.field("client_id", kakaoConfig.getClientId())
				.field("client_secret", kakaoConfig.getClientSecret())
				.field("redirect_uri", uri)
				.field("code", code)
				.asObject(KakaoTokenResponse.class);

		if (res.getStatus() != 200) {
			log.error(res.getBody().toString());
			//KakaoTokenResponse nres = res.getBody();
			String msg = String.format("카카오와 통신 중에 오류가 발생하여 작업을 중단하였습니다.");
			throw new CommunicationException(msg);
		}

		KakaoTokenResponse kres = res.getBody();
		return getKakaoMemberByAccessToken(kres.getAccess_token());
	}

	public LoginResult login_memberKakao(KakaoLoginParam param, String userIp) {
		if (param == null) {
			throw new InvalidationException("입력값이 올바르지 않습니다.");
		}

		boolean blankAccessToken = string.isBlank(param.getAccessToken());
		boolean blankCode = string.isBlank(param.getCode());
		boolean blankUri = string.isBlank(param.getUri());
		if (blankAccessToken && (blankCode || blankUri)) {
			throw new InvalidationException("입력값이 올바르지 않습니다.");
		}

		/*
		 * Kakao 사용자 정보 얻기
		 */
		KakaoLoginResponse nres = null;
		if (string.isNotBlank(param.getAccessToken())) {
			nres = getKakaoMemberByAccessToken(param.getAccessToken());
		}
		else {
			nres = getKakaoMemberByCode(param.getCode(), param.getUri());
		}

		/*
		 * 로그인 처리
		 */
        // Login ID/PW 를 기반으로 AuthenticationToken 생성
		CmmtMberInfo member = memberDao.selectByKakaoToken(nres.getId());
        if (member == null) {
        	throw new InvalidationException("카카오 계정 연동을 하지 않으셨습니다. 계정 연동을 먼저 하신 후 사용하세요.");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("sns:" + member.getLoginId(), member.getLoginId());

        // 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        // authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // RefreshToken 저장
        Date now = new Date();
        member.setRefreshToken(tokenDto.getRefreshToken());
        member.setLastLoginDt(now);
        memberDao.update(member);

        // 로그인 로그 생성
        saveLoginLog_member(member, userIp);

        // 비밀번호 변경 시점인가?
        int diffDay = date.getDiffDays(member.getPasswdDt(), now);
        boolean changePasswd = diffDay >= PASSWD_CHANGE_TERM;

        LoginResult result = new LoginResult();
        property.copyProperties(result, tokenDto);
        result.setChangePasswd(changePasswd);
        result.setInitPasswd(member.getPasswdInit());
        result.setDbid(member.getLoginId());
        result.setDbpw(member.getEncPasswd());

        return result;
	}

	public LoginResult login_evaluator(EvlLoginParam param, String userIp) {

    	if (param == null) {
    		throw new InvalidationException("입력이 없습니다.");
    	}

    	if (string.isBlank(param.getExpertNm())) {
    		throw new InvalidationException("평가위원 이름을 입력하세요.");
    	}
    	if (string.isBlank(param.getMobileNo())) {
    		throw new InvalidationException("휴대폰번호를 입력하세요.");
    	}
    	if (string.isBlank(param.getEmail())) {
    		throw new InvalidationException("이메일을 입력하세요.");
    	}
    	if (string.isBlank(param.getPasswd())) {
    		throw new InvalidationException("비밀번호를 입력하세요.");
    	}
    	if (string.isBlank(param.getEvlCmitId())) {
    		throw new InvalidationException("평가위원회를 입력하세요.");
    	}

    	/*
    	 * 전문가 조회
    	 * EXPERT_REQST_PROCESS_STTUS = ERPS02
    	 */
    	List<UsptExpert> expertList = usptExpertDao.selectList_expertNm(param.getExpertNm());
    	String mobileNo1 = string.getNumberOnly(param.getMobileNo());
    	String email1 = param.getEmail();
    	String mobileNo2 = null;
    	String email2 = null;
    	UsptExpert selExpert = null;
    	for (UsptExpert usptExpert : expertList) {
    		log.debug(usptExpert.getBrthdy() + "/" + usptExpert.getEmail() + "/" + usptExpert.getMbtlnum());
    		mobileNo2 = string.getNumberOnly(usptExpert.getMbtlnum());
    		email2 = usptExpert.getEmail();
    		boolean exists = string.equals(mobileNo1, mobileNo2) && string.equalsIgnoreCase(email1, email2);
    		if (exists) {
    			selExpert = usptExpert;
    			break;
    		}
    	}

    	if (selExpert == null) {
    		throw new InvalidationException("일치하는 전문가 정보가 없습니다.");
    	}

    	/*
    	 * 평가위원 조회
    	 */
    	EvlMfcmm evl = usptEvlMfcmmDao.selectByExpertId(param.getEvlCmitId(), selExpert.getExpertId());
    	if (evl == null) {
    		throw new InvalidationException("일치하는 평가위원 정보가 없습니다.");
    	}

    	boolean samePasswd = CoreUtils.password.compare(param.getPasswd(), evl.getEncPassword());
    	if (!samePasswd) {
    		throw new InvalidationException("비밀번호가 일치하지 않습니다.");
    	}


        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("evaluator:" + evl.getEvlMfcmmId(), param.getPasswd());

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        usptEvlMfcmmDao.updateRefreshToken(evl.getEvlMfcmmId(), tokenDto.getRefreshToken());

        // 로그인 로그 생성
        BnMember bnMember = usptEvlMfcmmDao.selectBnMember(evl.getEvlMfcmmId());
        if (bnMember == null) {
        	throw new InvalidationException("평가위원정보 또는 비밀번호가 올바르지 않습니다.");
        }
        saveLoginLog_evaluator(bnMember, userIp);

        // 5. 토큰 발급
        LoginResult result = new LoginResult();
        property.copyProperties(result, tokenDto);
        result.setChangePasswd(false);
        result.setInitPasswd(false);
        result.setDbid(bnMember.getLoginId());
        result.setDbpw(bnMember.getEncPasswd());

        return result;
	}

	public LoginResult login_insider(LoginParam param, String userIp) {
    	if (param == null || string.isBlank(param.getLoginId()) || string.isBlank(param.getPasswd())) {
    		throw new InvalidationException("아이디와 비밀번호를 모두 입력하세요.");
    	}

    	CmmtEmpInfo cmmtInsider = insiderDao.selectByLoginId(param.getLoginId());
    	if (cmmtInsider == null) {
        	throw new InvalidationException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

    	if (string.equals(cmmtInsider.getMemberSt(), CodeExt.memberSt.중지)) {
        	throw new InvalidationException("사용 중지된 계정입니다.\n관리자에게 문의하세요.");
    	}

        if (string.equals(cmmtInsider.getMemberSt(), CodeExt.memberSt.잠김)) {
        	throw new InvalidationException("비밀번호 연속 오류로 계정이 잠겼습니다.");
        }

        if (!string.equals(cmmtInsider.getMemberSt(), CodeExt.memberSt.정상)) {
        	throw new InvalidationException("로그인 계정에 대한 상태를 관리자에게 문의하세요.");
        }

        // 로그인시도 오래된 데이터 삭제
        cmmtLoginTryDao.deleteOld(cmmtInsider.getMemberId(), userIp);

        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("insider:" + param.getLoginId(), param.getPasswd());

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        CmmtEmpInfo insider = insiderDao.selectByLoginId(param.getLoginId());
        if (insider == null) {
        	throw new InvalidationException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        // 5. IP 확인
        if (string.isBlank(insider.getMemberIps())) {
        	throw new InvalidationException("사용자 IP가 등록되어 있지 않습니다.");
        }
        String[] ips = string.split(insider.getMemberIps(), '/');
        boolean ipOk = false;
        for (String ip : ips) {
        	if (antpath.match(ip, userIp) ) {
        		ipOk = true;
        		break;
        	}
        }
        if (!ipOk) {
        	throw new InvalidationException("접근이 허용된 IP가 아닙니다.");
        }

        Date now = new Date();
        insider.setRefreshToken(tokenDto.getRefreshToken());
        insider.setLastLoginDt(now);
        insiderDao.update(insider);

        // 로그인로그 생성
        saveLoginLog_insider(insider, userIp);

        /*
         * 로그인시도 이력 삭제
         */
        cmmtLoginTryDao.delete(cmmtInsider.getMemberId(), userIp);

        // 비밀번호 변경 시점인가?
        int diffDay = date.getDiffDays(insider.getPasswdDt(), now);
        boolean changePasswd = diffDay >= PASSWD_CHANGE_TERM;

        LoginResult result = new LoginResult();
        property.copyProperties(result, tokenDto);
        result.setChangePasswd(changePasswd);
        result.setInitPasswd(cmmtInsider.getPasswdInit());
        result.setDbid(cmmtInsider.getLoginId());
        result.setDbpw(cmmtInsider.getEncPasswd());

        /*
         * 이메일로 로그인 알림
         * (이메일 알림은 2022.10.01부터 메일 발송 수행)
         */
        if (string.isNotBlank(insider.getEmail()) && date.getDiffDays(string.toDate("20221001"), now) >= 0) {
	        String emailCn = CoreUtils.file.readFileString("/form/email/email-login-alert.html");
	        if (string.isBlank(emailCn)) {
	        	throw new LoggableException("인증메일 발송본문 파일을 읽을 수 없습니다.");
	        }
	        EmailSendParam eparam = new EmailSendParam();
	        eparam.setContentHtml(true);
	        eparam.setTitle("[인공지능산업융합사업단] 사용자지원포털 관리자페이지 로그인 알림");
	        eparam.setEmailCn(emailCn);

	        Map<String, String> templateParam = new HashMap<>();
			templateParam.put("loginId", param.getLoginId());
			templateParam.put("ip", userIp);
	        eparam.addRecipient(insider.getEmail(), insider.getMemberNm(), templateParam);

	        EmailResult er = emailUtils.send(eparam);
	        if (er.getSuccessCnt() == 0) {
				throw new LoggableException("인증메일 발송에 실패하였습니다.");
			}
        }

        // 5. 토큰 발급
        return result;
	}

	public void logout() {
		BnMember bnMember = SecurityUtils.getCurrentMember();
		if (bnMember == null) {
			return;
		}
		CmmtMberInfo member = memberDao.select(bnMember.getMemberId());
		if (member == null) {
			return;
		}
		member.setRefreshToken(null);
		memberDao.update(member);
	}

	public TokenDto refreshToken_member(HttpServletRequest request, HttpServletResponse response) {
		Cookie cookie = CoreUtils.webutils.getCookie(request, JwtUtils.cookieRefreshToken.회원용);
    	if (cookie == null) {
    		throw new UnauthorizedException();
    	}

    	String refreshToken = cookie.getValue();
    	TokenDto dto = jwtUtils.refreshToken_member(refreshToken);
    	if (dto == null) {
    		throw new UnauthorizedException();
    	}
		cookie = new Cookie(JwtUtils.cookieRefreshToken.회원용, dto.getRefreshToken());
		cookie.setMaxAge(envConfig.getCookie().getMaxAge());
		cookie.setSecure(envConfig.getCookie().getSecure());
		cookie.setHttpOnly(envConfig.getCookie().getHttpOnly());
		cookie.setDomain(envConfig.getCookie().getDomain());
		cookie.setPath(envConfig.getCookie().getPath());
		response.addCookie(cookie);

		response.setHeader("Set-AccessToken", dto.getAccessToken());
		response.setHeader("Set-RefreshTokenExpiresIn", "" + dto.getRefreshTokenExpiresIn());

        return dto;
	}

	public TokenDto refreshToken_insider(HttpServletRequest request, HttpServletResponse response) {
    	Cookie cookie = CoreUtils.webutils.getCookie(request, JwtUtils.cookieRefreshToken.내부사용자용);
    	if (cookie == null) {
    		throw new UnauthorizedException();
    	}

    	String refreshToken = cookie.getValue();

    	TokenDto dto = jwtUtils.refreshToken_insider(refreshToken);
    	if (dto == null) {
    		throw new UnauthorizedException();
    	}
    	cookie = new Cookie(JwtUtils.cookieRefreshToken.내부사용자용, dto.getRefreshToken());
    	cookie.setMaxAge(envConfig.getCookie().getMaxAge());
    	cookie.setSecure(envConfig.getCookie().getSecure());
    	cookie.setHttpOnly(envConfig.getCookie().getHttpOnly());
    	cookie.setDomain(envConfig.getCookie().getDomain());
    	cookie.setPath(envConfig.getCookie().getPath());
    	response.addCookie(cookie);

		response.setHeader("Set-AccessToken", dto.getAccessToken());
		response.setHeader("Set-RefreshTokenExpiresIn", "" + dto.getRefreshTokenExpiresIn());

        return dto;
	}

	public TokenDto refreshToken_evaluator(HttpServletRequest request, HttpServletResponse response) {
    	Cookie cookie = CoreUtils.webutils.getCookie(request, JwtUtils.cookieRefreshToken.회원용);
    	if (cookie == null) {
    		throw new UnauthorizedException();
    	}

    	String refreshToken = cookie.getValue();

    	TokenDto dto = jwtUtils.refreshToken_evaluator(refreshToken);
    	if (dto == null) {
    		throw new UnauthorizedException();
    	}
    	cookie = new Cookie(JwtUtils.cookieRefreshToken.회원용, dto.getRefreshToken());
    	cookie.setMaxAge(envConfig.getCookie().getMaxAge());
    	cookie.setSecure(envConfig.getCookie().getSecure());
    	cookie.setHttpOnly(envConfig.getCookie().getHttpOnly());
    	cookie.setDomain(envConfig.getCookie().getDomain());
    	cookie.setPath(envConfig.getCookie().getPath());
    	response.addCookie(cookie);

		response.setHeader("Set-AccessToken", dto.getAccessToken());
		response.setHeader("Set-RefreshTokenExpiresIn", "" + dto.getRefreshTokenExpiresIn());

        return dto;
	}
}
