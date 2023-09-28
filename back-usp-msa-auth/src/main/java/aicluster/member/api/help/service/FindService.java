package aicluster.member.api.help.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.member.api.help.dto.BzmnPasswdParam;
import aicluster.member.api.help.dto.CertCheckParam;
import aicluster.member.api.help.dto.ChangePasswdParam;
import aicluster.member.api.help.dto.EmailCertReqParam;
import aicluster.member.api.help.dto.FindBzmnIdParam;
import aicluster.member.api.help.dto.FindIndividualIdParam;
import aicluster.member.api.help.dto.IndividualPasswdParam;
import aicluster.member.api.help.dto.LoginIdDto;
import aicluster.member.api.help.dto.PasswdDto;
import aicluster.member.api.help.dto.PhoneCertReqParam;
import aicluster.member.common.dao.CmmtMberInfoDao;
import aicluster.member.common.dao.CmmtPasswordHistDao;
import aicluster.member.common.entity.CmmtMberInfo;
import aicluster.member.common.entity.CmmtPasswordHist;
import aicluster.member.common.util.CodeExt;
import aicluster.member.common.util.SessionAuthUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.masking;
import bnet.library.util.CoreUtils.password;
import bnet.library.util.CoreUtils.string;

@Service
public class FindService {

	@Autowired
	private CmmtMberInfoDao cmmtMemberDao;

	@Autowired
	private SessionAuthUtils sessionUtils;

	@Autowired
	private CmmtPasswordHistDao cmmtPasswdHistDao;

	public LoginIdDto findIndividualLoginId(FindIndividualIdParam param) {
		if (param == null) {
			throw new InvalidationException("입력이 없습니다.");
		}

		/*
		 * 입력검사
		 */

		InvalidationsException ies = new InvalidationsException();

		// 이름
		if (string.isBlank(param.getMemberNm())) {
			ies.add("memberNm", String.format(CodeExt.validateMessage.입력없음, "이름"));
		}

		// 생년월일
		param.setBirthday(string.getNumberOnly(param.getBirthday()));
		if (string.isBlank(param.getBirthday())) {
			ies.add("birthday", String.format(CodeExt.validateMessage.입력없음, "생년월일"));
		}

		// 휴대폰번호
		param.setMobileNo(string.getNumberOnly(param.getMobileNo()));
		if (string.isBlank(param.getMobileNo())) {
			ies.add("mobileNo", String.format(CodeExt.validateMessage.입력없음, "휴대폰번호"));
		}

		// 이메일
		if (string.isBlank(param.getEmail())) {
			ies.add("email", String.format(CodeExt.validateMessage.입력없음, "이메일"));
		}

		if (ies.size() > 0) {
			throw ies;
		}

		/*
		 * 아이디 찾기
		 */

		// 이름으로 조회
		List<CmmtMberInfo> memberList = cmmtMemberDao.selectByMemberNm(param.getMemberNm());

		String birthday = null;
		String mobileNo = null;
		String email = null;
		for( CmmtMberInfo cmmtMember : memberList ) {
			// 개인회원이 아니면 skip
			if ( !CodeExt.memberTypeExt.isIndividual(cmmtMember.getMemberType()) ) {
				continue;
			}

			// 생년월일 비교
			birthday = string.getNumberOnly(cmmtMember.getBirthday());
			if (!string.equals(birthday, param.getBirthday())) {
				continue;
			}

			// 휴대폰번호 비교
			mobileNo = string.getNumberOnly(cmmtMember.getMobileNo());
			if (!string.equals(mobileNo, param.getMobileNo())) {
				continue;
			}

			// 이메일 비교
			email = cmmtMember.getEmail();
			if (!string.equals(email, param.getEmail())) {
				continue;
			}

			/*
			 * 로그인 아이디 return
			 */
			String loginId = cmmtMember.getLoginId();
			loginId = masking.maskingLoginId(loginId);

			LoginIdDto dto = new LoginIdDto(loginId);
			return dto;
		}

		throw new InvalidationException("일치하는 회원정보가 없습니다.");
	}

	public LoginIdDto findBzmnLoginId(FindBzmnIdParam param) {
		if (param == null) {
			throw new InvalidationException("입력이 없습니다.");
		}

		/*
		 * 입력검사
		 */

		InvalidationsException ies = new InvalidationsException();

		// 이름(사업자명)
		if (string.isBlank(param.getMemberNm())) {
			ies.add("memberNm", String.format(CodeExt.validateMessage.입력없음, "이름"));
		}

		// 사업자번호
		param.setBizrno(string.getNumberOnly(param.getBizrno()));
		if (string.isBlank(param.getBizrno())) {
			ies.add("bizrno", String.format(CodeExt.validateMessage.입력없음, "사업자번호"));
		}

		// 휴대폰번호
		param.setMobileNo(string.getNumberOnly(param.getMobileNo()));
		if (string.isBlank(param.getMobileNo())) {
			ies.add("mobileNo", String.format(CodeExt.validateMessage.입력없음, "휴대폰번호"));
		}

		// 이메일
		if (string.isBlank(param.getEmail())) {
			ies.add("email", String.format(CodeExt.validateMessage.입력없음, "이메일"));
		}

		if (ies.size() > 0) {
			throw ies;
		}

		/*
		 * 아이디 찾기
		 */

		// 이름으로 조회
		List<CmmtMberInfo> memberList = cmmtMemberDao.selectByMemberNm(param.getMemberNm());

		String bizrno = null;
		String mobileNo = null;
		String email = null;
		for( CmmtMberInfo cmmtMember : memberList ) {
			// 개인회원이면 skip
			if ( CodeExt.memberTypeExt.isIndividual(cmmtMember.getMemberType()) ) {
				continue;
			}

			// 사업자번호 비교
			bizrno = string.getNumberOnly(cmmtMember.getBizrno());
			if (!string.equals(bizrno, param.getBizrno())) {
				continue;
			}

			// 휴대폰번호 비교
			mobileNo = string.getNumberOnly(cmmtMember.getMobileNo());
			if (!string.equals(mobileNo, param.getMobileNo())) {
				continue;
			}

			// 이메일 비교
			email = cmmtMember.getEmail();
			if (!string.equals(email, param.getEmail())) {
				continue;
			}

			/*
			 * 로그인 아이디 return
			 */
			String loginId = cmmtMember.getLoginId();
			loginId = masking.maskingLoginId(loginId);

			LoginIdDto dto = new LoginIdDto(loginId);
			return dto;
		}

		throw new InvalidationException("일치하는 회원정보가 없습니다.");
	}

	public PasswdDto individualPasswd(IndividualPasswdParam param) {
		if (param == null) {
			throw new InvalidationException("입력이 없습니다.");
		}

		/*
		 * 입력검사
		 */
		InvalidationsException ies = new InvalidationsException();

		// 로그인 아이디
		String loginId = string.trim(param.getLoginId());
		if (string.isBlank(loginId)) {
			ies.add("loginId", "로그인 아이디를 입력하세요.");
		}

		// 이름
		String memberNm = string.trim(param.getMemberNm());
		if (string.isBlank(memberNm)) {
			ies.add("memberNm", "이름을 입력하세요.");
		}

		// 생년월일
		Date birthDt = string.toDate(param.getBirthday());
		String birthday = null;
		if (birthDt != null) {
			birthday = date.format(birthDt, "yyyyMMdd");
		}
		if (string.isBlank(birthday)) {
			ies.add("birthday", "생년월일을 입력하세요.");
		}

		// 이메일
		String email = string.trim(param.getEmail());
		if (string.isBlank(email)) {
			ies.add("email", "이메일을 입력하세요.");
		}

		// 휴대폰번호
		String mobileNo = string.getNumberOnly(param.getMobileNo());
		if (string.isBlank(mobileNo)) {
			ies.add("mobileNo", "휴대폰번호를 입력하세요.");
		}

		if (ies.size() > 0) {
			throw ies;
		}

		/*
		 * 데이터 비교
		 */

		// 아이디로 회원정보 조회
		InvalidationException ie = new InvalidationException("일치하는 회원정보가 없습니다.");
		CmmtMberInfo cmmtMember = cmmtMemberDao.selectByLoginId(loginId);
		if (cmmtMember == null) {
			throw ie;
		}

		// 개인사용자 여부 확인
		if ( !CodeExt.memberTypeExt.isIndividual(cmmtMember.getMemberType()) ) {
			throw ie;
		}

		// 회원명 비교
		if (!string.equalsIgnoreCase(memberNm, cmmtMember.getMemberNm())) {
			throw ie;
		}

		// 생녕월일 비교
		if (!string.equals(birthday, cmmtMember.getBirthday())) {
			throw ie;
		}

		// email 비교
		if (!string.equalsIgnoreCase(email, cmmtMember.getEmail())) {
			throw ie;
		}

		// 휴대폰번호 비교
		String orgMobileNo = string.getNumberOnly(cmmtMember.getMobileNo());
		if (!string.equals(mobileNo, orgMobileNo)) {
			throw ie;
		}

		// 키 생성
		String key = sessionUtils.findPassword.set(cmmtMember.getMemberId(), mobileNo, email);

		// return
		mobileNo = string.toPhoneFormat(mobileNo);
		PasswdDto dto = PasswdDto.builder()
				.key(key)
				.mobileNo(mobileNo)
				.email(email)
				.build();

		return dto;
	}

	public PasswdDto bzmnPasswd(BzmnPasswdParam param) {
		/*
		 * 입력검사
		 */
		if (param == null) {
			throw new InvalidationException("입력이 없습니다.");
		}

		InvalidationsException ies = new InvalidationsException();

		// 로그인 아이디
		String loginId = string.trim(param.getLoginId());
		if (string.isBlank(loginId)) {
			ies.add("loginId", "로그인 아이디를 입력하세요.");
		}

		// 이름
		String memberNm = string.trim(param.getMemberNm());
		if (string.isBlank(memberNm)) {
			ies.add("memberNm", "사업자명을 입력하세요.");
		}

		// 사업자등록번호
		String bizrno = string.trim(param.getBizrno());
		if (string.isBlank(bizrno)) {
			ies.add("bizrno", "사업자등록번호를 입력하세요.");
		}

		// 이메일
		String email = string.trim(param.getEmail());
		if (string.isBlank(email)) {
			ies.add("email", "이메일을 입력하세요.");
		}

		// 휴대폰번호
		String mobileNo = string.getNumberOnly(param.getMobileNo());
		if (string.isBlank(mobileNo)) {
			ies.add("mobileNo", "휴대폰번호를 입력하세요.");
		}

		if (ies.size() > 0) {
			throw ies;
		}

		/*
		 * 데이터 비교
		 */
		InvalidationException ie = new InvalidationException("일치하는 회원정보가 없습니다.");

		CmmtMberInfo cmmtMember = cmmtMemberDao.selectByLoginId(loginId);
		if (cmmtMember == null) {
			throw ie;
		}

		// 사업자 확인
		if (!CodeExt.memberTypeExt.isBzmn(cmmtMember.getMemberType())) {
			throw ie;
		}

		// 이름 비교
		if (!string.equalsIgnoreCase(memberNm, cmmtMember.getMemberNm())) {
			throw ie;
		}

		// 사업자등록번호 비교
		if (!string.equalsIgnoreCase(bizrno, cmmtMember.getBizrno())) {
			throw ie;
		}

		// 이메일 비교
		if (!string.equalsIgnoreCase(email, cmmtMember.getEmail())) {
			throw ie;
		}

		// 휴대폰번호 비교
		String orgMobileNo = string.getNumberOnly(cmmtMember.getMobileNo());
		if (!string.equalsIgnoreCase(mobileNo, orgMobileNo)) {
			throw ie;
		}

		// 키 생성
		String key = sessionUtils.findPassword.set(cmmtMember.getMemberId(), mobileNo, email);

		// return
		mobileNo = string.toPhoneFormat(mobileNo);

		PasswdDto dto = PasswdDto.builder()
				.key(key)
				.mobileNo(mobileNo)
				.email(email)
				.build();

		return dto;
	}

	public void phoneCertReq(PhoneCertReqParam param) {
		if (param == null) {
			throw new InvalidationException("입력이 없습니다.");
		}

		String mobileNo = string.getNumberOnly(param.getMobileNo());

		/*
		 * 검사
		 */
		String orgMobileNo = sessionUtils.findPassword.getMobileNo(param.getKey());
		if (!string.equals(mobileNo, orgMobileNo)) {
			throw new InvalidationException("등록된 휴대폰 번호와 동일하지 않습니다.");
		}

		/*
		 * 인증번호 생성, 세션 저장
		 */
		String certNo = string.getRandomNo(6);

		/*
		 * 인증번호 발송
		 */
		sessionUtils.mobileSmsNoCert.sendSms(mobileNo, certNo);

		// Session 저장
		sessionUtils.findPassword.setCertNo(param.getKey(), certNo);
	}

	public void certCheck(CertCheckParam param) {
		if (param == null) {
			throw new InvalidationException("입력이 없습니다.");
		}

		/*
		 * 인증번호 검사
		 */
		boolean same = sessionUtils.findPassword.check(param.getKey(), param.getCertNo());
		if (!same) {
			throw new InvalidationException("인증번호가 일치하지 않습니다.");
		}
	}

	public void emailCertReq(EmailCertReqParam param) {
		if (param == null) {
			throw new InvalidationException("입력이 없습니다.");
		}

		String email = string.deleteWhitespace(param.getEmail());

		/*
		 * 검사
		 */
		String orgEmail = sessionUtils.findPassword.getEmail(param.getKey());
		if (!string.equals(email, orgEmail)) {
			throw new InvalidationException("등록된 이메일 주소와 동일하지 않습니다.");
		}

		/*
		 * 인증번호 생성
		 */
		String certNo = string.getRandomNo(6);

		/*
		 * 인증번호 발송
		 */
		sessionUtils.emailCert.sendMail(email, email, certNo);

		// Session 저장
		sessionUtils.findPassword.setCertNo(param.getKey(), certNo);
	}

	public void changePasswd(ChangePasswdParam param) {
		/*
		 * 입력확인
		 */
		if (param == null) {
			throw new InvalidationException("입력이 없습니다.");
		}

		if (string.isBlank(param.getPasswd1()) || string.isBlank(param.getPasswd2())) {
			throw new InvalidationException("비밀번호를 모두 입력하세요.");
		}

		/*
		 * Session 값 조회
		 */
		String memberId = sessionUtils.findPassword.getMemberId(param.getKey());
		CmmtMberInfo cmmtMember = cmmtMemberDao.select(memberId);
		if (cmmtMember == null) {
			throw new InvalidationException("올바른 사용법이 아닙니다(2)");
		}

		/*
		 * 비밀번호 확인
		 */
		if (!string.equals(param.getPasswd1(), param.getPasswd2())) {
			throw new InvalidationException("비밀번호 2개가 일치하지 않습니다. 다시 입력하세요.");
		}

		// 비밀번호 규칙 확인
		List<CmmtPasswordHist> passwdHistList = cmmtPasswdHistDao.selectList_recent(memberId, 3);
		CodeExt.passwd.checkValidation(param.getPasswd1(), cmmtMember.getBirthday(), passwdHistList);

		/*
		 * 비밀번호 수정
		 */
		Date now = new Date();
		String encPasswd = password.encode(param.getPasswd1());
		cmmtMember.setPasswdInit(false);
		cmmtMember.setEncPasswd(encPasswd);
		cmmtMember.setPasswdDt(now);

		cmmtMemberDao.update(cmmtMember);

		/*
		 * 비밀번호 이력 추가
		 */
		CmmtPasswordHist pwdHist = CmmtPasswordHist.builder()
				.memberId(memberId)
				.histDt(now)
				.encPasswd(encPasswd)
				.build();

		cmmtPasswdHistDao.insert(pwdHist);
	}
}
