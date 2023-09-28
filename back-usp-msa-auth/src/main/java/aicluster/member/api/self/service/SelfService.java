package aicluster.member.api.self.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.dto.SessionKeyDto;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.util.NiceIdUtils;
import aicluster.framework.common.util.SessionUtils;
import aicluster.framework.common.util.dto.NiceIdConfig;
import aicluster.framework.common.util.dto.NiceIdResult;
import aicluster.framework.security.SecurityUtils;
import aicluster.member.api.module.dto.NiceIdResultParam;
import aicluster.member.api.self.dto.ModifyPasswdParam;
import aicluster.member.api.self.dto.PasswdCheckRes;
import aicluster.member.api.self.dto.SelfModifyParam;
import aicluster.member.common.dao.CmmtMberInfoDao;
import aicluster.member.common.dao.CmmtPasswordHistDao;
import aicluster.member.common.dto.MemberSelfDto;
import aicluster.member.common.entity.CmmtMberInfo;
import aicluster.member.common.entity.CmmtPasswordHist;
import aicluster.member.common.util.CodeExt;
import aicluster.member.common.util.CodeExt.memberTypeExt;
import aicluster.member.common.util.CodeExt.validateMessageExt;
import aicluster.member.common.util.SessionAuthUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.property;
import bnet.library.util.CoreUtils.string;

@Service
public class SelfService {

    @Autowired
    private NiceIdConfig niceIdConfig;

    @Autowired
    private NiceIdUtils niceIdUtils;

	@Autowired(required = false)
	private CmmtMberInfoDao cmmtMemberDao;
	@Autowired
	private SessionAuthUtils sessionUtils;
	@Autowired
	private SessionUtils fwSessionUtils;
	@Autowired
	private CmmtPasswordHistDao cmmtPasswdHistDao;

	/**
	 * 로그인 사용자 비밀번호 확인
	 *
	 * @param passwd
	 * @return
	 */
	public PasswdCheckRes passwdCheck(String passwd) {
		// 작업자 확인
		BnMember worker = SecurityUtils.checkLogin();

		CmmtMberInfo cmmtMember = cmmtMemberDao.select(worker.getMemberId());
		if (cmmtMember == null) {
			throw new InvalidationException("회원정보를 찾을 수 없습니다.");
		}

		// 비밀번호 확인
		if (!CoreUtils.password.compare(passwd, cmmtMember.getEncPasswd())) {
			throw new InvalidationException("비밀번호가 일치하지 않습니다.");
		}

		// session 저장
		String sessionId = sessionUtils.passwdCheck.set(cmmtMember.getMemberId());

		// return 값 생성
		return PasswdCheckRes.builder()
				.passwdCheckKey(sessionId)
				.build();
	}

	/**
	 * 로그인 사용자 정보 조회(비밀번호 확인 검증)
	 *
	 * @param passwdCheckKey
	 * @return
	 */
	public MemberSelfDto getMeForChkPasswd(String passwdCheckKey) {
		// 작업자 확인
		BnMember worker = SecurityUtils.checkLogin();

		// session 조회
		if (string.isBlank(passwdCheckKey)) {
			throw new InvalidationException("비밀번호검사 Key를 입력하세요");
		}

		String memberId = sessionUtils.passwdCheck.get(passwdCheckKey);
		if (!string.equals(worker.getMemberId(), memberId) ) {
			throw new InvalidationException("올바른 사용법이 아닙니다.");
		}

		// 사용자 정보 조회
		MemberSelfDto memberSelfDto = getMe();

		// Session 시간 update
		fwSessionUtils.updateExpiredDt(passwdCheckKey);

		return memberSelfDto;
	}

	/**
	 * 로그인 사용자 정보 변경
	 *
	 * @param passwdCheckKey
	 * @param param
	 * @return
	 */
	public MemberSelfDto modify(String passwdCheckKey, SelfModifyParam param) {
		// 회원구분 검사
		BnMember worker = SecurityUtils.checkLogin();

		/*
		 * 비밀번호 확인 여부 검사
		 */
		String memberId = sessionUtils.passwdCheck.get(passwdCheckKey);
		if (!string.equals(memberId, worker.getMemberId())) {
			throw new InvalidationException("올바른 사용법이 아닙니다.");
		}

		/*
		 * 회원정보 수정
		 */

		// Session 시간 연장
		fwSessionUtils.updateExpiredDt(passwdCheckKey);

		// 사업자회원 정보 수정
		if (CodeExt.memberTypeExt.isBzmn(worker.getMemberType())) {
			return modifyBzmn(worker.getMemberId(), param);
		}
		// 개인회원 정보 수정
		else if (CodeExt.memberTypeExt.isIndividual(worker.getMemberType())) {
			return modifyIndividual(worker.getMemberId(), param.getMarketingReception());
		}

		throw new InvalidationException("회원구분이 올바르지 않습니다.");
	}

	/**
	 * 로그인 개인회원 사용자정보 변경
	 *
	 * @param memberId
	 * @param marketingReception
	 * @return
	 */
	private MemberSelfDto modifyIndividual(String memberId, Boolean marketingReception) {
		CmmtMberInfo cmmtMember = cmmtMemberDao.select(memberId);
		if (cmmtMember == null) {
			throw new InvalidationException("회원정보가 없습니다.");
		}

		if (!CodeExt.memberTypeExt.isIndividual(cmmtMember.getMemberType())) {
			throw new InvalidationException("개인사용자가 아닙니다.");
		}

		if (marketingReception == null) {
			throw new InvalidationException("마케팅 정보 수신 동의 여부를 입력하세요.");
		}

		Date now = new Date();
		cmmtMember.setMarketingReception(marketingReception);
		cmmtMember.setUpdatedDt(now);
		cmmtMember.setUpdaterId(memberId);

		cmmtMemberDao.update(cmmtMember);

		cmmtMember = cmmtMemberDao.select(memberId);

		MemberSelfDto selfDto = new MemberSelfDto();
		property.copyProperties(selfDto, cmmtMember);

		return selfDto;
	}

	/**
	 * 로그인 사업자회원 사용자정보 변경
	 *
	 * @param memberId
	 * @param param
	 * @return
	 */
	private MemberSelfDto modifyBzmn(String memberId, SelfModifyParam param) {
		CmmtMberInfo cmmtMember = cmmtMemberDao.select(memberId);
		if (cmmtMember == null) {
			throw new InvalidationException("회원정보가 없습니다.");
		}

		if (!CodeExt.memberTypeExt.isBzmn(cmmtMember.getMemberType())) {
			throw new InvalidationException("사업자가 아닙니다.");
		}

		InvalidationsException ies = new InvalidationsException();

		// 사업자명 검사
		if (string.isBlank(param.getMemberNm())) {
			ies.add("memberNm", "사업자명을 입력하세요.");
		}
		// 대표자명 검사
		if (string.isBlank(param.getCeoNm())) {
			ies.add("ceoNm", "대표자명을 입력하세요.");
		}

		// 담당자명 검사
		if (string.isBlank(param.getChargerNm())) {
			ies.add("chargerNm", "담당자명을 입력하세요.");
		}

		// 마케팅 정보 수신 동의 검사
		if (param.getMarketingReception() == null) {
			ies.add("marketingReception", "마케팅 정보 수신 동의 여부를 입력하세요.");
		}

		if (ies.size() > 0) {
			throw ies;
		}

		Date now = new Date();
		cmmtMember.setMemberNm(param.getMemberNm());
		cmmtMember.setCeoNm(param.getCeoNm());
		cmmtMember.setChargerNm(param.getChargerNm());
		cmmtMember.setMarketingReception(param.getMarketingReception());
		cmmtMember.setUpdatedDt(now);
		cmmtMember.setUpdaterId(memberId);

		cmmtMemberDao.update(cmmtMember);

		cmmtMember = cmmtMemberDao.select(memberId);

		MemberSelfDto selfDto = new MemberSelfDto();
		property.copyProperties(selfDto, cmmtMember);

		return selfDto;
	}

	/**
	 * 로그인 사업자회원 휴대폰 인증번호 요청
	 *
	 * @param passwdCheckKey	비밀번호 검증 세션ID
	 * @param mobileNo	휴대폰 번호
	 * @return	휴대폰 SMS 인증번호 저장 세션ID
	 */
	public SessionKeyDto phoneCertReq(String passwdCheckKey, String mobileNo)
	{
		BnMember worker = SecurityUtils.checkLogin();

		// 사업자만 사용가능
		if (!CodeExt.memberTypeExt.isBzmn(worker.getMemberType())) {
			throw new InvalidationException("사업자 회원이어야 사용가능합니다.");
		}

		// passwdCheckKey 확인
		String memberId = sessionUtils.passwdCheck.get(passwdCheckKey);
		if (!string.equals(worker.getMemberId(), memberId)) {
			throw new InvalidationException("올바른 사용법이 아닙니다.");
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
		String sessionId = sessionUtils.mobileSmsNoCert.set(mobileNo, certNo);

		return new SessionKeyDto(sessionId);
	}

	/**
	 * 로그인 사업자회원 휴대폰 인증번호 확인
	 *
	 * @param passwdCheckKey	비밀번호 검증 세션ID
	 * @param phoneCertKey	휴대폰 SMS 인증번호 저장 세션ID
	 * @param certNo	입력된 인증번호
	 */
	public void phoneCertCheck(String passwdCheckKey, String phoneCertKey, String certNo) {
		BnMember worker = SecurityUtils.checkLogin();

		// passwdCheckKey 확인
		String memberId = sessionUtils.passwdCheck.get(passwdCheckKey);
		if (!string.equals(worker.getMemberId(), memberId)) {
			throw new InvalidationException("올바른 사용법이 아닙니다.");
		}

		// 휴대폰 인증번호 확인
		boolean same = sessionUtils.mobileSmsNoCert.check(phoneCertKey, certNo);
		if (!same) {
			throw new InvalidationException("인증번호가 일치하지 않습니다.");
		}
	}

	/**
	 * 로그인 사업자회원 휴대폰 번호 변경
	 *
	 * @param passwdCheckKey	비밀번호 검증 세션ID
	 * @param phoneCertKey	휴대폰 SMS 인증번호 세션ID
	 */
	public void modifyMobileNo(String passwdCheckKey, String phoneCertKey)
	{
		BnMember worker = SecurityUtils.checkLogin();

		// 사업자여야 수정가능
		if (!CodeExt.memberTypeExt.isBzmn(worker.getMemberType())) {
			throw new InvalidationException("사업자 회원이어야 사용가능합니다.");
		}

		// passwdCheckKey 확인
		String memberId = sessionUtils.passwdCheck.get(passwdCheckKey);
		if (!string.equals(worker.getMemberId(), memberId)) {
			throw new InvalidationException("올바른 사용법이 아닙니다.");
		}

		// 휴대폰번호 조회
		String mobileNo = sessionUtils.mobileSmsNoCert.getMobileNo(phoneCertKey);

		/*
		 * 휴대폰번호 저장
		 */
		CmmtMberInfo cmmtMember = cmmtMemberDao.select(worker.getMemberId());
		if (cmmtMember == null) {
			throw new InvalidationException("회원정보가 없습니다.");
		}

		Date now = new Date();
		String encMobileNo = CoreUtils.aes256.encrypt(mobileNo, worker.getMemberId());
		cmmtMember.setEncMobileNo(encMobileNo);
		cmmtMember.setUpdatedDt(now);
		cmmtMember.setUpdaterId(worker.getMemberId());

		cmmtMemberDao.update(cmmtMember);
	}

	/**
	 * 로그인 사용자 이메일 인증 요청
	 *
	 * @param passwdCheckKey
	 * @param email
	 * @return
	 */
	public SessionKeyDto emailCertReq(String passwdCheckKey, String email)
	{
		BnMember worker = SecurityUtils.checkLogin();

		// passwdCheckKey 확인
		String memberId = sessionUtils.passwdCheck.get(passwdCheckKey);
		if (!string.equals(worker.getMemberId(), memberId)) {
			throw new InvalidationException("올바른 사용법이 아닙니다.");
		}

        // 인증번호 생성
        String certNo = string.getRandomNo(6);

		/*
		 * 이메일 인증번호 발송
		 */
		sessionUtils.emailCert.sendMail(worker.getMemberNm(), email, certNo);

		// 인증번호 session에 저장
		String sessionId = sessionUtils.emailCert.set(email, certNo);

		SessionKeyDto dto = new SessionKeyDto(sessionId);
		return dto;
	}

	/**
	 * 로그인 사용자 이메일 인증번호 확인
	 *
	 * @param passwdCheckKey
	 * @param emailCertKey
	 * @param certNo
	 */
	public void emailCertCheck(String passwdCheckKey, String emailCertKey, String certNo)
	{
		BnMember worker = SecurityUtils.checkLogin();

		// passwdCheckKey 확인
		String memberId = sessionUtils.passwdCheck.get(passwdCheckKey);
		if (!string.equals(worker.getMemberId(), memberId)) {
			throw new InvalidationException("올바른 사용법이 아닙니다.");
		}

		// 이메일 인증번호 확인
		boolean same = sessionUtils.emailCert.check(emailCertKey, certNo);
		if (!same) {
			throw new InvalidationException("인증번호가 일치하지 않습니다.");
		}
	}

	/**
	 * 로그인 사용자 이메일 변경
	 *
	 * @param passwdCheckKey
	 * @param emailCertKey
	 */
	public void modifyEmail(String passwdCheckKey, String emailCertKey) {
		BnMember worker = SecurityUtils.checkLogin();

		// passwdCheckKey 확인
		String memberId = sessionUtils.passwdCheck.get(passwdCheckKey);
		if (!string.equals(worker.getMemberId(), memberId)) {
			throw new InvalidationException("올바른 사용법이 아닙니다.");
		}

		// 이메일 조회
		String email = sessionUtils.emailCert.getEmail(emailCertKey);

		/*
		 * 이메일 저장
		 */
		CmmtMberInfo cmmtMember = cmmtMemberDao.select(worker.getMemberId());
		if (cmmtMember == null) {
			throw new InvalidationException("회원정보가 없습니다.");
		}

		Date now = new Date();
		String encEmail = CoreUtils.aes256.encrypt(email, worker.getMemberId());
		cmmtMember.setEncEmail(encEmail);
		cmmtMember.setUpdatedDt(now);
		cmmtMember.setUpdaterId(worker.getMemberId());

		cmmtMemberDao.update(cmmtMember);
	}

	/**
	 * 로그인 사용자 비밀번호 변경
	 *
	 * @param param
	 */
	public void modifyPasswd(ModifyPasswdParam param) {
		BnMember worker = SecurityUtils.checkLogin();

		CmmtMberInfo cmmtMember = cmmtMemberDao.select(worker.getMemberId());
		if (cmmtMember == null) {
			throw new InvalidationException("회원정보가 없습니다.");
		}

		/*
		 * 비밀번호 검사
		 */

		// 현재 비밀번호 검사
		boolean same = CoreUtils.password.compare(param.getOldPasswd(), cmmtMember.getEncPasswd());
		if (!same) {
			throw new InvalidationException("현재 비밀번호가 일치하지 않습니다.");
		}

		// 새 비밀번호 2개가 일치하는 지 검사
		same = string.equals(param.getNewPasswd1(), param.getNewPasswd2());
		if (!same) {
			throw new InvalidationException("새 비밀번호가 동일하지 않습니다.");
		}

		// 비밀번호 조건 검사
		// 새 비밀번호 적합성 검사
		List<CmmtPasswordHist> passwdHistList = cmmtPasswdHistDao.selectList_recent(cmmtMember.getMemberId(), 3);
		CodeExt.passwd.checkValidation(param.getNewPasswd1(), cmmtMember.getBirthday(), passwdHistList);


		// 비밀번호 저장
		Date now = new Date();
		String encPasswd = CoreUtils.password.encode(param.getNewPasswd1());
		cmmtMember.setEncPasswd(encPasswd);
		cmmtMember.setPasswdDt(now);
		cmmtMember.setPasswdInit(false);
		cmmtMember.setUpdatedDt(now);
		cmmtMember.setUpdaterId(worker.getMemberId());

		cmmtMemberDao.update(cmmtMember);

		/*
		 * 비밀번호 이력 추가
		 */
		CmmtPasswordHist pwdHist = CmmtPasswordHist.builder()
				.memberId(cmmtMember.getMemberId())
				.histDt(now)
				.encPasswd(encPasswd)
				.build();

		cmmtPasswdHistDao.insert(pwdHist);
	}

	/**
	 * 로그인 사용자 회원정보 조회
	 *
	 * @return 로그인 사용자정보
	 */
	public MemberSelfDto getMe() {
		// 작업자 확인
		BnMember worker = SecurityUtils.checkLogin();

		// 사용자 정보 조회
		MemberSelfDto memberSelfDto = cmmtMemberDao.selectMe(worker.getMemberId());
		if(memberSelfDto == null) {
			throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "자료"));
		}

		// 회원유형별 출력 항목 정리
		if (memberTypeExt.isBzmn(memberSelfDto.getMemberType())) {
			memberSelfDto.setGender(null);
			memberSelfDto.setEncBirthday(null);

			// 개인사업자인 경우 법인등록번호 null 처리
			if (string.equals(memberSelfDto.getMemberType(), memberTypeExt.개인사업자)) {
				memberSelfDto.setJurirno(null);
			}
		}
		// 사업자 회원이 아닌 경우 아래 항목 Null 처리
		else {
			memberSelfDto.setChargerNm(null);
			memberSelfDto.setCeoNm(null);
			memberSelfDto.setBizrno(null);
			memberSelfDto.setJurirno(null);
		}

		return memberSelfDto;
	}

	/**
	 * 휴대폰 본인인증 후 휴대폰 번호 변경
	 *
	 * @param param : 휴대폰 본인인증(Nice) 결과 암호문
	 */
	public void modifyMobileCert(NiceIdResultParam param)
	{
        Date now = new Date();
        BnMember worker = SecurityUtils.checkLogin();
        NiceIdResult result = niceIdUtils.getResult(niceIdConfig, param.getEncodeData());

        // 로그인 사용자 개인회원 검증
        if (!CodeExt.memberTypeExt.isIndividual(worker.getMemberType())) {
        	throw new InvalidationException("개인회원만 휴대폰 본인인증 후 휴대폰 번호 변경이 가능합니다.");
        }

		/*
		 * 비밀번호 확인 여부 검사
		 */
		String memberId = sessionUtils.passwdCheck.get(param.getSessionId());
		if (!string.equals(memberId, worker.getMemberId())) {
			throw new InvalidationException("올바른 사용법이 아닙니다.");
		}

        /*
         * CI(result.getConnInfo())를 이용해 회원정보를 조회한다.
         */
        CmmtMberInfo member = cmmtMemberDao.selectByCi(result.getConnInfo());
        if (member == null) {
        	throw new InvalidationException(String.format(validateMessageExt.조회결과없음, "가입된 회원정보"));
        }

        // 로그인 사용자와 조회정보 사이에 회원ID 일치여부 검증
        if (!string.equals(worker.getMemberId(), member.getMemberId())) {
        	throw new InvalidationException("로그인 계정과 인증정보가 일치하지 않습니다.");
        }

        // 휴대폰 번호 변경
        member.setEncMobileNo(aes256.encrypt(result.getMobileNo(), worker.getMemberId()));
        member.setUpdaterId(worker.getMemberId());
        member.setUpdatedDt(now);

        // 회원정보 update
        cmmtMemberDao.update(member);
	}

}
