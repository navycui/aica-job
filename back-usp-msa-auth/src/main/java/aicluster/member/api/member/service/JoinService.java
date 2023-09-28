package aicluster.member.api.member.service;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RegExUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.dto.SessionConsentDto;
import aicluster.framework.common.dto.SessionKeyDto;
import aicluster.framework.common.util.NiceIdUtils;
import aicluster.framework.common.util.SessionUtils;
import aicluster.framework.common.util.TermsUtils;
import aicluster.framework.common.util.dto.NiceIdConfig;
import aicluster.framework.common.util.dto.NiceIdResult;
import aicluster.framework.common.util.dto.PkiResult;
import aicluster.member.api.member.dto.CertResultForJoinDto;
import aicluster.member.api.member.dto.JoinParam;
import aicluster.member.api.member.dto.MemberGetListParam;
import aicluster.member.api.member.dto.PkiCertParam;
import aicluster.member.api.module.dto.NiceIdResultParam;
import aicluster.member.common.dao.CmmtMberInfoDao;
import aicluster.member.common.dao.CmmtMberInfoHistDao;
import aicluster.member.common.dto.MemberSelfDto;
import aicluster.member.common.dto.VerifyLoginIdResultDto;
import aicluster.member.common.entity.CmmtMberInfo;
import aicluster.member.common.entity.CmmtMberInfoHist;
import aicluster.member.common.util.CodeExt;
import aicluster.member.common.util.CodeExt.passwd;
import aicluster.member.common.util.CodeExt.validateMessageExt;
import aicluster.member.common.util.SessionAuthUtils;
import aicluster.member.common.util.dto.SessionIdSetForJoinDto;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.password;
import bnet.library.util.CoreUtils.property;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.CoreUtils.validation;

@Service
public class JoinService {

    @Autowired
    private NiceIdConfig niceIdConfig;

    @Autowired
    private NiceIdUtils niceIdUtils;

    @Autowired
    private SessionAuthUtils sessionUtils;

    @Autowired
    private SessionUtils fwSessionUtils;

    @Autowired
    private TermsUtils termsUtils;

    @Autowired
    private CmmtMberInfoDao memberDao;

    @Autowired
    private CmmtMberInfoHistDao memberHistDao;

    /**
     * 이메일 인증번호 발송
     *
     * @param email
     * @return
     */
    public SessionKeyDto emailCertReq(String email) {
        email = string.trim(email);
        if (string.isBlank(email)) {
            throw new InvalidationException("이메일을 입력하세요.");
        }

        if (!validation.isEmail(email)) {
            throw new InvalidationException("이메일이 올바르지 않습니다. 확인하십시오.");
        }

        // 인증번호 생성
        String certNo = string.getRandomNo(6);

		/*
		 * 인증번호 발송
		 */
        sessionUtils.emailCert.sendMail(email, email, certNo);

        // Session 저장
        String sessionId = sessionUtils.emailCert.set(email, certNo);

        // 결과 리턴
        return new SessionKeyDto(sessionId);
    }

    /**
     * 이메일 인증번호 검증
     *
     * @param emailCertKey
     * @param certNo
     */
    public void emailCertCheck(String emailCertKey, String certNo) {
        // 입력확인
        if (string.isBlank(emailCertKey)) {
            throw new InvalidationException("세션ID를 입력하세요.");
        }
        if (string.isBlank(certNo)) {
            throw new InvalidationException("인증번호를 입력하세요.");
        }

        // session 확인
        boolean same = sessionUtils.emailCert.check(emailCertKey, certNo);
        if (!same) {
            throw new InvalidationException("인증번호가 일치하지 않습니다.");
        }
    }

    /**
     * 휴대폰 SMS 인증번호 요청(사업자 공동인증서 인증한 경우에만 허용)
     *
     * @param sessionId	회원가입 인증 Session ID
     * @param mobileNo	휴대폰 번호
     * @return	휴대폰 SMS 인증번호 저장한 Session ID
     */
	public SessionKeyDto phoneCertReq(String sessionId, String mobileNo)
	{
        // sessionId가 회원가입 세션정보 결과에 해당하는 'joinset-'으로 시작되는지 확인한다.
        if (string.isBlank(sessionId) || !string.startsWith(sessionId, CodeExt.prefix.가입세선ID)) {
        	throw new InvalidationException("공동인증서 인증을 수행하세요..");
        }

		// 사업자만 사용가능하므로 공동인증서 인증결과 세션ID가 있는지 검증한다.
        SessionIdSetForJoinDto joinDto = sessionUtils.joinSessId.get(sessionId);
        if ( string.isBlank(joinDto.getPkiCertSessionId()) ) {
            throw new InvalidationException("공동인증서 인증을 수행하세요.");
        }

        mobileNo = string.getNumberOnly(mobileNo);
		if (string.isBlank(mobileNo)) {
			throw new InvalidationException("휴대폰번호를 입력하세요.");
		}

		/*
		 * 인증번호 SMS 발송
		 */
		String certNo = string.getRandomNo(6);
		sessionUtils.mobileSmsNoCert.sendSms(mobileNo, certNo);

		// 인증번호 Session에 저장
		String phoneSessionId = sessionUtils.mobileSmsNoCert.set(mobileNo, certNo);

		return new SessionKeyDto(phoneSessionId);
	}

	/**
	 * 휴대폰 SMS 인증번호 확인(사업자 공동인증서 인증한 경우에만 허용)
	 *
	 * @param sessionId	회원가입 인증 Session ID
	 * @param phoneCertKey	휴대폰 SMS 인증번호 저장한 Session ID
	 * @param certNo	인증번호
	 */
	public void phoneCertCheck(String sessionId, String phoneCertKey, String certNo)
	{
        // sessionId가 회원가입 세션정보 결과에 해당하는 'joinset-'으로 시작되는지 확인한다.
        if (string.isBlank(sessionId) || !string.startsWith(sessionId, CodeExt.prefix.가입세선ID)) {
        	throw new InvalidationException("회원가입 인증 정보가 없습니다. 공동인증서 인증을 수행하세요.");
        }

		// 사업자만 사용가능하므로 공동인증서 인증결과 세션ID가 있는지 검증한다.
        SessionIdSetForJoinDto joinDto = sessionUtils.joinSessId.get(sessionId);
        if ( string.isBlank(joinDto.getPkiCertSessionId()) ) {
            throw new InvalidationException("공동인증서 인증을 수행하세요.");
        }

		// 휴대폰 SMS 인증번호 확인
		boolean same = sessionUtils.mobileSmsNoCert.check(phoneCertKey, certNo);
		if (!same) {
			throw new InvalidationException("SMS 인증번호가 일치하지 않습니다.");
		}
	}

    /**
     * 로그인ID 중복 검증
     *
     * @param loginId
     * @return
     */
    public VerifyLoginIdResultDto verifyLoginId(String loginId)
    {
        // 입력값 검증
        if (string.isBlank(loginId)) {
            throw new InvalidationException(String.format(validateMessageExt.입력없음, "로그인ID"));
        }
    	// 로그인ID 적합성 검증
    	if ( !CodeExt.loginId.isValid(loginId) ) {
            throw new InvalidationException(validateMessageExt.아이디적합성오류);
    	}

        // 로그인ID 건수 조회
    	MemberGetListParam param = new MemberGetListParam();
    	param.setLoginId(loginId);
        long duplicateCnt = memberDao.selectCount(param);

        // 출력 DTO 정의
        VerifyLoginIdResultDto verifyDto = new VerifyLoginIdResultDto();
        verifyDto.setLoginId(loginId);

        // 로그인ID 건수에 따른 중복여부 세팅
        if (duplicateCnt > 0) {
            verifyDto.setDuplicateYn(true);
        }
        else {
            verifyDto.setDuplicateYn(false);
        }

        return verifyDto;
    }

    /**
     * 휴대폰 본인인증 처리
     *
     * @param param
     * @return
     */
    public CertResultForJoinDto verifyMobileCert(NiceIdResultParam param)
    {
        // sessionId가 회원가입 세션정보 결과에 해당하는 'joinset-'으로 시작되는 경우 부모 본인인증으로 판단한다.
        if (string.startsWith(param.getSessionId(), CodeExt.prefix.가입세선ID)) {
            return prcsParentCert(param);
        }

        // 1단계 본인인증 결과 처리
        return prcsFirstCert(param);
    }

    /**
     * 1단계 본인인증결과 처리
     *
     * @param param
     * @return
     */
    private CertResultForJoinDto prcsFirstCert(NiceIdResultParam param)
    {
        Date now = new Date();
        NiceIdResult result = niceIdUtils.getResult(niceIdConfig, param.getEncodeData());

        /*
         * CI(result.getConnInfo())를 이용해 이미 가입해 있는 지를 확인한다.
         */
        boolean isSecession = false;
        boolean isDuplicate = false;
        boolean isChild = false;
        String loginId = null;
        String sessionId = null;
        CmmtMberInfo member = memberDao.selectByCi(result.getConnInfo());
        if (member != null) {
            isDuplicate = true;
            loginId = member.getLoginId();

            if (string.equals(member.getMemberSt(), CodeExt.memberSt.탈퇴)) {
                if (date.getDiffDays(member.getMemberStDt(), now) <= 7) {
                    isSecession = true;
                }
            }
        }

        // 14세 미만 여부
        if (date.getAge(result.getBirthDate()) < 14) {
            isChild = true;
        }

        // 신규 가입자이거나 탈퇴회원계정 경우에만 세션 테이블에 저장한다.
        if (BooleanUtils.isFalse(isDuplicate) || BooleanUtils.isTrue(isSecession)) {
            SessionIdSetForJoinDto joinDto = new SessionIdSetForJoinDto();
            joinDto.setChildYn(isChild);

            // 휴대폰 본인인증 결과정보 Session 테이블 저장
            String certRstSesionId = fwSessionUtils.mobileCertSession.set(result);
            joinDto.setMobileCertSessionId(certRstSesionId);

            // 탈퇴회원인 경우 약관동의이력정보 만료 처리
            if (isSecession) {
                // 약관동의이력정보 임시저장 Session 만료
            	fwSessionUtils.expire(param.getSessionId());
                joinDto.setTermsConsentSessionId(null);
            }
            else {
                // 약관동의이력정보 임시저장 Session 만료일시 연장(30분)
            	fwSessionUtils.updateExpiredDt(param.getSessionId());
                joinDto.setTermsConsentSessionId(param.getSessionId());
            }

            // 약관동의이력정보 Session ID와 휴대폰 본인인증 결과정보 Session ID를 모아서 신규 Session 테이블에 저장
            sessionId = sessionUtils.joinSessId.set(joinDto);
        }
        else {
            // 약관동의이력 임시저장 session 만료 처리
        	fwSessionUtils.expire(param.getSessionId());
        }

        // Session ID와 탈퇴회원여부 출력
        CertResultForJoinDto dto = new CertResultForJoinDto();
        dto.setKey(sessionId);
        dto.setChildYn(isChild);
        dto.setDuplicateYn(isDuplicate);
        dto.setSecessionYn(isSecession);
        dto.setLoginId(loginId);

        return dto;
    }

    /**
     * 부모 본인인증결과 처리
     *
     * @param param
     * @return
     */
    private CertResultForJoinDto prcsParentCert(NiceIdResultParam param)
    {
        NiceIdResult parentResult = niceIdUtils.getResult(niceIdConfig, param.getEncodeData());
        // 보호자 휴대폰인증 18세 이상 체크
        if (date.getAge(parentResult.getBirthDate()) < 18) {
            throw new InvalidationException("18세 미만인 본인인증은 보호자 본인인증 대상이 아닙니다.");
        }

        SessionIdSetForJoinDto joinDto = sessionUtils.joinSessId.get(param.getSessionId());
        if (!joinDto.isChildYn()) {
            throw new InvalidationException("보호자 휴대폰인증 대상이 아닙니다.");
        }

        if (string.isNotBlank(joinDto.getParentMobileCertSessionId())) {
            throw new InvalidationException("보호자 휴대폰인증이 이미 존재합니다.");
        }

        NiceIdResult childResult = fwSessionUtils.mobileCertSession.get(joinDto.getMobileCertSessionId());
        if (string.equals(childResult.getConnInfo(), parentResult.getConnInfo())) {
            throw new InvalidationException("회원가입하려는 자의 휴대폰 본인인증과 보호자 휴대폰본인인 결과가 동일합니다. 회원가입을 처음부터 다시 진행하세요.");
        }

        if (string.equals(childResult.getMobileNo(), parentResult.getMobileNo())) {
            throw new InvalidationException("회원가입하려는 자의 휴대폰 번호와 보호자 휴대폰 번호는 동일할 수 없습니다.");
        }

        // 기존 회원가입 세션정보 만료처리
        fwSessionUtils.expire(param.getSessionId());

        // 약관동의이력정보 Session 만료일시 연장(30분)
        fwSessionUtils.updateExpiredDt(joinDto.getTermsConsentSessionId());

        // 회원가입대상 휴대폰 본인인증 결과 DTO Session 만료일시 연장
//		sessionUtils.updateExpiredDt(joinDto.getMobileCertSessionId(), 60);

        // 보호자 본인인증결과 DTO Session 입력
        String parentCertSessionId = fwSessionUtils.mobileCertSession.set(parentResult);
        joinDto.setParentMobileCertSessionId(parentCertSessionId);

        // 새로운 회원가입 세션정보 session 입력
        String joinSessionId = sessionUtils.joinSessId.set(joinDto);

        // Session ID와 탈퇴회원여부 출력
        CertResultForJoinDto dto = new CertResultForJoinDto();
        dto.setKey(joinSessionId);

        return dto;
    }

    /**
     * 공동인증서 인증결과를 세션으로부터 조회하여 회원가입 인증 처리
     *
     * @param param	약관동의 및 공동인증서 인증결과 세션ID 모음
     * @return	회원가입 세션ID
     */
    public CertResultForJoinDto verifyPkiCert(PkiCertParam param)
    {
        Date now = new Date();

		// 공동인증서 인증결과 정보를 세션 테이블로부터 조회
        PkiResult result = fwSessionUtils.pkiCertSession.check(param.getPkiCertSessionId());

        /*
         * CI(result.getIdentifyData())를 이용해 이미 가입해 있는 지를 확인한다.
         */
        boolean isSecession = false;
        boolean isDuplicate = false;
        boolean isChild = false;
        String loginId = null;
        String chargerNm = null;
        String sessionId = null;
        CmmtMberInfo member = memberDao.selectByCi(result.getIdentifyData());
        if (member != null) {
            isDuplicate = true;
            loginId = member.getLoginId();
            chargerNm = member.getChargerNm();

            if (string.equals(member.getMemberSt(), CodeExt.memberSt.탈퇴)) {
                if (date.getDiffDays(member.getMemberStDt(), now) <= 7) {
                    isSecession = true;
                }
            }
        }

        // 신규 가입자이거나 탈퇴회원계정 경우에만 세션 테이블에 저장한다.
        if (BooleanUtils.isFalse(isDuplicate) || BooleanUtils.isTrue(isSecession)) {
            SessionIdSetForJoinDto joinDto = new SessionIdSetForJoinDto();
            joinDto.setChildYn(isChild);

            // 공동인증서 결과정보 SessionID를 회원가입 DTO 저장
            joinDto.setPkiCertSessionId(param.getPkiCertSessionId());

            // 탈퇴회원인 경우 약관동의이력정보 만료 처리
            if (isSecession) {
                // 약관동의이력정보 임시저장 Session 만료
            	fwSessionUtils.expire(param.getTermsConsentSessionId());
                joinDto.setTermsConsentSessionId(null);
            }
            else {
                // 약관동의이력정보 임시저장 Session 만료일시 연장(30분)
            	fwSessionUtils.updateExpiredDt(param.getTermsConsentSessionId());
                joinDto.setTermsConsentSessionId(param.getTermsConsentSessionId());
            }

            // 약관동의이력정보 Session ID와 휴대폰 본인인증 결과정보 Session ID를 모아서 신규 Session 테이블에 저장
            sessionId = sessionUtils.joinSessId.set(joinDto);
        }
        else {
            // 약관동의이력 임시저장 session 만료 처리
        	fwSessionUtils.expire(param.getTermsConsentSessionId());
        }

        // Session ID와 탈퇴회원여부 출력
        CertResultForJoinDto dto = new CertResultForJoinDto();
        dto.setKey(sessionId);
        dto.setChildYn(isChild);
        dto.setDuplicateYn(isDuplicate);
        dto.setSecessionYn(isSecession);
        dto.setLoginId(loginId);
        dto.setChargerNm(chargerNm);

        return dto;
    }

    /**
     * 회원가입 처리
     *
     * @param param
     */
    public MemberSelfDto join(JoinParam param)
    {
        boolean isIndividual = true;  // 개인회원여부

        if (string.isBlank(param.getSessionId()) || !string.startsWith(param.getSessionId(), CodeExt.prefix.가입세선ID)) {
            throw new InvalidationException("인증을 수행하지 않았습니다. 인증을 수행하세요.");
        }

        SessionIdSetForJoinDto joinDto = sessionUtils.joinSessId.get(param.getSessionId());

        if (string.isBlank(joinDto.getTermsConsentSessionId())) {
            throw new InvalidationException("약관동의를 하지 않았습니다. 약관동의를 수행하세요.");
        }

        if ( string.isBlank(joinDto.getMobileCertSessionId()) && string.isBlank(joinDto.getPkiCertSessionId()) ) {
            throw new InvalidationException("휴대폰 본인인증 또는 공동인증을 수행하세요.");
        }

        if ( string.isNotBlank(joinDto.getMobileCertSessionId()) && string.isNotBlank(joinDto.getPkiCertSessionId()) ) {
            throw new InvalidationException("휴대폰 본인인증과 공동인증을 같이 사용할 수 없습니다. 회원가입을 처음부터 다시 진행하세요.");
        }

        // 휴대폰 본인인증 Session ID가 없고 공동인증 Session ID가 있는 경우 '개인회원여부(isIndividual)'을 false 처리한다.
        if ( string.isBlank(joinDto.getMobileCertSessionId()) && string.isNotBlank(joinDto.getPkiCertSessionId()) ) {
            isIndividual = false;
        }

        // 개인회원인 경우 휴대폰 인증으로부터 정보 도출
        // 사업회원인 경우 공동인증으로부터 정보 도출
        String birthday = null;		// 생년월일
        String gender = null;		// 성별
        String ci = null;			// 인증CI
        String bizrno = null;		// 사업자등록번호
        if ( BooleanUtils.isTrue(isIndividual) ) {
            NiceIdResult mobileResult = fwSessionUtils.mobileCertSession.get(joinDto.getMobileCertSessionId());

            if ( BooleanUtils.isTrue(joinDto.isChildYn()) ) {

                if (string.isBlank(joinDto.getParentMobileCertSessionId())) {
                    throw new InvalidationException("보호자 휴대폰 인증을 수행하세요.");
                }

                NiceIdResult parentResult = fwSessionUtils.mobileCertSession.get(joinDto.getParentMobileCertSessionId());

                if (date.getAge(parentResult.getBirthDate()) < 18) {
                    throw new InvalidationException("18세 미만인 본인인증은 보호자 휴대폰 인증 대상이 아닙니다.");
                }
                if (string.equals(mobileResult.getConnInfo(), parentResult.getConnInfo())) {
                    throw new InvalidationException("회원가입하려는 자의 휴대폰 본인인증과 보호자 휴대폰본인인 결과가 동일합니다. 회원가입을 처음부터 다시 진행하세요.");
                }

                if (string.equals(mobileResult.getMobileNo(), parentResult.getMobileNo())) {
                    throw new InvalidationException("회원가입하려는 자의 휴대폰 번호와 보호자 휴대폰 번호는 동일할 수 없습니다.");
                }
            }

            birthday = mobileResult.getBirthDate();

            if (string.equals(mobileResult.getGender(), "1")) {
            	gender = CodeExt.gender.남성;
            }
            else {
            	gender = CodeExt.gender.여성;
            }

            ci = mobileResult.getConnInfo();

            param.setMobileNo(mobileResult.getMobileNo());
            param.setMemberNm(mobileResult.getName());
            param.setMemberType(CodeExt.memberTypeExt.개인);
        }
        else {
        	// 공동인증서 인증결과 정보를 세션 테이블로부터 조회
        	PkiResult pkiResult = fwSessionUtils.pkiCertSession.get(joinDto.getPkiCertSessionId());

        	gender = CodeExt.gender.없음;
        	ci = pkiResult.getIdentifyData();
        	bizrno = pkiResult.getBizrno();
        }

		// CMMT_SESSION으로부터 약관동의정보 조회
		List<SessionConsentDto> consentList = fwSessionUtils.termsConsentSession.get(joinDto.getTermsConsentSessionId());

        // 개인정보수집동의 선택 동의여부 세팅(개인정보수집동의서 - 선택을 동의한 경우에는 true, 그외에는 false로 한다)
		SessionConsentDto 개인정보수집_선택_동의정보 = null;
		if (BooleanUtils.isTrue(isIndividual)) {
			개인정보수집_선택_동의정보 = consentList.stream()
										.filter(consent -> string.equals(consent.getTermsType(), "PRVC_CLCT_AGRE_MBR")
												&& consent.isRequired() == false)
										.findFirst().get();
		}
		else {
			개인정보수집_선택_동의정보 = consentList.stream()
										.filter(consent -> string.equals(consent.getTermsType(), "PRVC_CLCT_AGRE_BIZ")
												&& consent.isRequired() == false)
										.findFirst().get();
		}

        // 입력 값 검증
        InvalidationsException errs = new InvalidationsException();

        // 로그인ID 검증
        if (string.isBlank(param.getLoginId())) {
            errs.add("loginId", String.format(validateMessageExt.입력없음, "아이디"));
        }
        else {
        	// 로그인ID 적합성 검증
        	if ( !CodeExt.loginId.isValid(param.getLoginId()) ) {
        		errs.add("loginId", validateMessageExt.아이디적합성오류);
        	}
        }

        CmmtMberInfo member = memberDao.selectByLoginId( param.getLoginId() );
        if (member != null) {
        	errs.add("loginId", "사용할 수 없는 아이디입니다. 다른 아이디를 입력하세요.");
        }

        if (string.isBlank(param.getPasswd1())) {
            errs.add("passwd1", String.format(validateMessageExt.입력없음, "비밀번호"));
        }
        if (string.isBlank(param.getPasswd2())) {
            errs.add("passwd2", String.format(validateMessageExt.입력없음, "비밀번호 확인"));
        }
        if (!string.equals(param.getPasswd1(), param.getPasswd2())) {
            errs.add("passwd2", "비밀번호가 동일하지 않습니다.");
        }

        if (string.isBlank(param.getEmail())) {
        	errs.add("email", String.format(validateMessageExt.입력없음, "이메일"));
        }

        // 사업자 회원인 경우 추가 검증
        if (BooleanUtils.isFalse(isIndividual)) {
        	if (string.isBlank(param.getMemberType())) {
        		errs.add("memberType", String.format(validateMessageExt.체크없음, "사업자 유형"));
        	}
        	if (!CodeExt.memberTypeExt.isBzmn(param.getMemberType())) {
        		errs.add("memberType", String.format(validateMessageExt.유효오류, "사업자 유형"));
        	}
        	if (string.isBlank(param.getMemberNm())) {
        		errs.add("memberNm", String.format(validateMessageExt.입력없음, "사업자명"));
        	}
        	if (string.isBlank(param.getCeoNm())) {
        		errs.add("ceoNm", String.format(validateMessageExt.입력없음, "대표자명"));
        	}
        	// 개인사업자가 아니고 개인정보수집-선택 동의한 경우 법인번호 필수 체크
        	if (!string.equals(param.getMemberType(), CodeExt.memberTypeExt.개인사업자) && 개인정보수집_선택_동의정보.isConsentYn() ) {
            	if (string.isBlank(param.getJurirno())) {
            		errs.add("jurirno", String.format(validateMessageExt.입력없음, "법인등록번호"));
            	}
            	if (!CoreUtils.validation.isBubinNumber(param.getJurirno())) {
        			errs.add("jurirno", "법인번호가 올바르지 않습니다.");
        		}
        	}
        	if (string.isBlank(param.getChargerNm())) {
        		errs.add("chargerNm", String.format(validateMessageExt.입력없음, "담당자"));
        	}
        	if (string.isBlank(param.getMobileNo())) {
        		errs.add("mobileNo", String.format(validateMessageExt.입력없음, "휴대폰 번호"));
        	}
        }

        if (errs.size() > 0) {
        	throw errs;
        }

        // 비밀번호 검증
        passwd.checkValidation(param.getPasswd1(), birthday);

        // 회원ID 생성
        String memberId = string.getNewId(CodeExt.prefix.회원ID);

        // 닉네임 생성(홍*동)
        int memberNmLen = param.getMemberNm().length();
		StringBuffer nickname = new StringBuffer(param.getMemberNm().substring(0, 1));
		nickname.append(RegExUtils.replaceAll(param.getMemberNm().substring(1, memberNmLen-1), Pattern.compile("[\\S+]"), "*"));
		nickname.append(param.getMemberNm().substring(memberNmLen-1));

        // 회원 Entity 생성
        Date now = new Date();
        member = CmmtMberInfo.builder()
						.memberId(memberId)
						.loginId(param.getLoginId())
						.encPasswd(password.encode(param.getPasswd1()))
						.passwdDt(now)
						.passwdInit(false)
						.memberNm(param.getMemberNm())
						.nickname(nickname.toString())
						.gender(gender)
						.authorityId(CodeExt.authorityId.일반사용자)
						.memberSt(CodeExt.memberSt.정상)
						.memberStDt(now)
						.memberType(param.getMemberType())
						.ci(ci)
						.encEmail(aes256.encrypt(param.getEmail(), memberId))
						.encBirthday(aes256.encrypt(birthday, memberId))
						.encMobileNo(aes256.encrypt(param.getMobileNo(), memberId))
						.chargerNm(param.getChargerNm())
						.ceoNm(param.getCeoNm())
						.bizrno(bizrno)
						.jurirno(param.getJurirno())
						.instr(false)
						.creatorId(memberId)
						.createdDt(now)
						.updaterId(memberId)
						.updatedDt(now)
						.build();

		// 개인정보수집-선택 미동의한 경우 선택 항목 값 세팅
		if (!개인정보수집_선택_동의정보.isConsentYn()) {
			// 사업자회원 선택항목 Null 또는 false 처리 (소속부서, 직위, 전화번호, 팩스번호, 법인등록번호)
			if (BooleanUtils.isFalse(isIndividual)) {
				// 법인등록번호 항목 NULL
				member.setJurirno(null);
			}
			// 회원에 대한 마케팅정보수신여부 false 처리
			member.setMarketingReception(false);
		}

        // CMMT_MEMBER 테이블 insert
        memberDao.insert(member);

        // 약관동의정보 생성
        termsUtils.insertList(joinDto.getTermsConsentSessionId(), memberId);

        // 인증 세션정보 만료 처리
        if ( string.isNotBlank(joinDto.getMobileCertSessionId())) {
        	fwSessionUtils.expire(joinDto.getMobileCertSessionId());
        }
        if ( string.isNotBlank(joinDto.getPkiCertSessionId()) ) {
        	fwSessionUtils.expire(joinDto.getPkiCertSessionId());
        }
        if ( string.isNotBlank(joinDto.getParentMobileCertSessionId()) ) {
        	fwSessionUtils.expire(joinDto.getParentMobileCertSessionId());
        }
        if ( string.isNoneBlank(joinDto.getTermsConsentSessionId()) ) {
        	fwSessionUtils.expire(joinDto.getTermsConsentSessionId());
        }
        fwSessionUtils.expire(param.getSessionId());

        // 가입된 회원정보를 재조회한다.
        member = memberDao.select(memberId);
        MemberSelfDto selfDto = new MemberSelfDto();
        property.copyProperties(selfDto, member);

        return selfDto;
    }

    /**
     * 탈퇴계정에 대한 정상 전환
     *
     * @param sessionId: 본인인증 결과 session ID
     */
	public void unsecession(String sessionId)
	{
        if (string.isBlank(sessionId) || !string.startsWith(sessionId, CodeExt.prefix.가입세선ID)) {
            throw new InvalidationException("인증을 수행하지 않았습니다. 인증을 수행하세요.");
        }

        SessionIdSetForJoinDto joinDto = sessionUtils.joinSessId.get(sessionId);

        if ( string.isBlank(joinDto.getMobileCertSessionId()) && string.isBlank(joinDto.getPkiCertSessionId()) ) {
            throw new InvalidationException("휴대폰 본인인증 또는 공동인증을 수행하세요.");
        }

        if ( string.isNotBlank(joinDto.getMobileCertSessionId()) && string.isNotBlank(joinDto.getPkiCertSessionId()) ) {
            throw new InvalidationException("휴대폰 본인인증과 공동인증을 같이 사용할 수 없습니다. 회원가입을 처음부터 다시 진행하세요.");
        }

        // 인증 수단으로부터 인증 CI값 도출
        String ci = "";
        if ( string.isNotBlank(joinDto.getMobileCertSessionId()) ) {
            NiceIdResult mobileResult = fwSessionUtils.mobileCertSession.get(joinDto.getMobileCertSessionId());

            ci = mobileResult.getConnInfo();
        }
        else {
        	// 공동인증서 인증결과 정보를 세션 테이블로부터 조회
        	PkiResult pkiResult = fwSessionUtils.pkiCertSession.get(joinDto.getPkiCertSessionId());

        	ci = pkiResult.getIdentifyData();
        }

        // 회원정보 조회
        Date now = new Date();
        CmmtMberInfo member = memberDao.selectByCi(ci);
        if (member == null) {
        	throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "탈퇴계정 정보"));
        }

        if (!string.equals(member.getMemberSt(), CodeExt.memberSt.탈퇴)) {
        	throw new InvalidationException("탈퇴계정인 경우에만 정상계정으로 전환이 가능합니다.");
        }

        if (date.getDiffDays(member.getMemberStDt(), now) > 7) {
        	throw new InvalidationException("탈퇴 7일 이내인 경우에만 정상계정으로 전환이 가능합니다.");
        }

        // 회원정보에 대한 회원상태값을 '정상'으로 변경
        member.setMemberSt(CodeExt.memberSt.정상);
        member.setMemberStDt(now);
        member.setUpdaterId(member.getMemberId());
        member.setUpdatedDt(now);

        // 회원정보 Update
        memberDao.update(member);

        // 회원이력정보 생성
        CmmtMberInfoHist hist = CmmtMberInfoHist.builder()
        							.histId(string.getNewId(CodeExt.prefix.이력ID))
        							.histDt(now)
        							.workTypeNm("탈퇴정상전환")
        							.memberId(member.getMemberId())
        							.workerId(member.getMemberId())
        							.workCn("회원가입의 본인인증 수행 후 탈퇴 계정을 정상전환하였습니다.")
        							.build();
        // 회원이력정보 insert
        memberHistDao.insert(hist);

        // 인증 세션정보 만료 처리
        if ( string.isNotBlank(joinDto.getMobileCertSessionId())) {
        	fwSessionUtils.expire(joinDto.getMobileCertSessionId());
        }
        if ( string.isNotBlank(joinDto.getPkiCertSessionId()) ) {
        	fwSessionUtils.expire(joinDto.getPkiCertSessionId());
        }
        if ( string.isNotBlank(joinDto.getParentMobileCertSessionId()) ) {
        	fwSessionUtils.expire(joinDto.getParentMobileCertSessionId());
        }
        if ( string.isNoneBlank(joinDto.getTermsConsentSessionId()) ) {
        	fwSessionUtils.expire(joinDto.getTermsConsentSessionId());
        }
        fwSessionUtils.expire(sessionId);
	}
}
