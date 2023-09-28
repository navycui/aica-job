package aicluster.member.api.self.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.dto.SessionConsentDto;
import aicluster.framework.common.dto.SessionKeyDto;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.util.SessionUtils;
import aicluster.framework.common.util.TermsUtils;
import aicluster.framework.common.util.dto.PkiResult;
import aicluster.framework.security.SecurityUtils;
import aicluster.member.api.member.dto.PkiCertParam;
import aicluster.member.api.self.dto.ChangeParam;
import aicluster.member.common.dao.CmmtMberInfoDao;
import aicluster.member.common.entity.CmmtMberInfo;
import aicluster.member.common.util.CodeExt;
import aicluster.member.common.util.SessionAuthUtils;
import aicluster.member.common.util.CodeExt.validateMessageExt;
import aicluster.member.common.util.dto.SessionIdSetForBzmnDto;
import bnet.library.exception.InvalidationException;
import bnet.library.exception.InvalidationsException;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.string;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BzmnService {

	@Autowired
	private CmmtMberInfoDao cmmtMemberDao;

	@Autowired
	private SessionAuthUtils sessionUtils;

	@Autowired
	private SessionUtils fwSessionUtils;

    @Autowired
    private TermsUtils termsUtils;

	/**
	 * 사업자전환 대상자 회원유형 검증
	 *
	 * @param param
	 * @return Token 회원정보
	 */
	private BnMember checkWorkerIsChgBizTrgt() {
		BnMember worker = SecurityUtils.checkWorkerIsMember();
		if ( !CodeExt.memberTypeExt.isIndividual(worker.getMemberType()) && !string.equals(worker.getMemberType(), CodeExt.memberTypeExt.개인사업자) ) {
			throw new InvalidationException("개인사용자 또는 개인사업자만 사용할 수 있습니다.");
		}

		return worker;
	}

	/**
	 * 사업자전환 세션ID 만료 처리
	 * @param bzmnDto
	 */
	private void expiredSessionId(SessionIdSetForBzmnDto bzmnDto)
	{
		// 약관동의 임시저장 세션 만료 처리
		if ( string.isNotBlank(bzmnDto.getTermsConsentSessionId()) ) {
			fwSessionUtils.expire(bzmnDto.getTermsConsentSessionId());
		}
		// 공동인증서 인증결과 세션 만료 처리
		if ( string.isNotBlank(bzmnDto.getPkiCertSessionId()) ) {
			fwSessionUtils.expire(bzmnDto.getPkiCertSessionId());
		}
		// 휴대폰번호 인증번호 세션 만료 처리
		if ( string.isNotBlank(bzmnDto.getPhoneCertSessionId()) ) {
			fwSessionUtils.expire(bzmnDto.getPhoneCertSessionId());
		}
		// 이메일 인증번호 세션 만료 처리
		if ( string.isNotBlank(bzmnDto.getEmailCertSessionId()) ) {
			fwSessionUtils.expire(bzmnDto.getEmailCertSessionId());
		}
	}

	private SessionIdSetForBzmnDto getBzmnDto(String sessionId, BnMember worker)
	{
		if ( !string.startsWith(sessionId, CodeExt.prefix.사업자전환세션ID) ) {
			throw new InvalidationException("비정상적인 접근입니다.");
		}

		// 사업자전환 세션ID 모음 조회
		SessionIdSetForBzmnDto bzmnDto = sessionUtils.bzmnSessId.get(sessionId);

		if ( bzmnDto == null ) {
			fwSessionUtils.expire(sessionId);
			throw new InvalidationException("인증을 다시 수행하세요.");
		}
		// 개인회원인 경우 약관동의 세션 검증
		if ( CodeExt.memberTypeExt.isIndividual(worker.getMemberType()) ) {
			if ( string.isBlank(bzmnDto.getTermsConsentSessionId()) ) {
				expiredSessionId(bzmnDto);
				throw new InvalidationException("사업자전환 약관동의를 진행하세요.");
			}
		}
		if ( string.isBlank(bzmnDto.getPkiCertSessionId()) ) {
			expiredSessionId(bzmnDto);
			throw new InvalidationException("공동인증서 인증을 진행하세요.");
		}

		return bzmnDto;
	}

	/**
	 * 휴대폰 SMS 인증번호 요청
	 * @param sessionId	사업자전환 세션ID
	 * @param mobileNo	휴대폰 번호
	 * @return	휴대폰 SMS 인증번호 저장 세션ID가 저장된 사업자전환 세션ID
	 */
	public SessionKeyDto phoneCertReq(String sessionId, String mobileNo)
	{
		BnMember worker = checkWorkerIsChgBizTrgt();

		// 사업자전환 세션ID 모음 조회
		SessionIdSetForBzmnDto bzmnDto = getBzmnDto(sessionId, worker);

		mobileNo = string.getNumberOnly(mobileNo);
		if (string.isBlank(mobileNo)) {
			throw new InvalidationException("휴대폰번호를 입력하세요");
		}

		/*
		 * 인증번호 발송
		 */
		String certNo = string.getRandomNo(6);
		sessionUtils.mobileSmsNoCert.sendSms(mobileNo, certNo);

		/*
		 * 인증번호 저장
		 */
		String phoneSessionId = sessionUtils.mobileSmsNoCert.set(mobileNo, certNo);
		bzmnDto.setPhoneCertSessionId(phoneSessionId);

		// 사업자전환 세션ID 새로 저장
		String bzmnSessionId = sessionUtils.bzmnSessId.set(bzmnDto);

		// 기존 세션ID 만료 처리
		fwSessionUtils.expire(sessionId);

		/*
		 * Key return
		 */
		return new SessionKeyDto(bzmnSessionId);
	}

	/**
	 * 휴대폰 SMS 인증번호 확인
	 * @param sessionId	사업자전환 세션ID
	 * @param certNo	입력된 인증번호
	 */
	public void phoneCertCheck(String sessionId, String certNo)
	{
		BnMember worker = checkWorkerIsChgBizTrgt();

		// 사업자전환 세션ID 모음 조회
		SessionIdSetForBzmnDto bzmnDto = getBzmnDto(sessionId, worker);

		// 입력검사
		if (string.isBlank(bzmnDto.getPhoneCertSessionId())) {
			throw new InvalidationException("휴대폰 인증번호 호출을 재진행하세요.");
		}
		if (string.isBlank(certNo)) {
			throw new InvalidationException("인증번호를 입력하세요.");
		}

		// 인증번호 검사
		boolean same = sessionUtils.mobileSmsNoCert.check(bzmnDto.getPhoneCertSessionId(), certNo);
		if (!same) {
			throw new InvalidationException("인증번호가 일치하지 않습니다.");
		}
	}

	/**
	 * 이메일 인증번호 요청
	 * @param sessionId	사업자전환 세션ID
	 * @param email	이메일
	 * @return	이메일 인증번호 저장 세션ID가 저장된 사업자전환 세션ID
	 */
	public SessionKeyDto emailCertReq(String sessionId, String email)
	{
		BnMember worker = checkWorkerIsChgBizTrgt();

		// 사업자전환 세션ID 모음 조회
		SessionIdSetForBzmnDto bzmnDto = getBzmnDto(sessionId, worker);

		email = string.removeWhitespace(email);
		if (string.isBlank(email)) {
			throw new InvalidationException("이메일을 입력하세요");
		}

        // 인증번호 생성
        String certNo = string.getRandomNo(6);

		/*
		 * 인증번호 발송
		 */
		sessionUtils.emailCert.sendMail(worker.getMemberNm(), email, certNo);

		/*
		 * 인증번호 저장
		 */
		String emailSessionId = sessionUtils.emailCert.set(email, certNo);
		bzmnDto.setEmailCertSessionId(emailSessionId);

		// 사업자전환 세션ID 새로 저장
		String bzmnSessionId = sessionUtils.bzmnSessId.set(bzmnDto);

		// 기존 세션ID 만료 처리
		fwSessionUtils.expire(sessionId);

		/*
		 * Key return
		 */
		return new SessionKeyDto(bzmnSessionId);
	}

	/**
	 * 이메일 인증번호 확인
	 * @param sessionId	사업자전환 세션ID
	 * @param certNo	입력된 인증번호
	 */
	public void emailCertCheck(String sessionId, String certNo)
	{
		BnMember worker = checkWorkerIsChgBizTrgt();

		// 사업자전환 세션ID 모음 조회
		SessionIdSetForBzmnDto bzmnDto = getBzmnDto(sessionId, worker);

		/*
		 * 입력검사
		 */
		if (string.isBlank(bzmnDto.getEmailCertSessionId())) {
			throw new InvalidationException("이메일 인증번호 요청을 재수행하세요.");
		}
		if (string.isBlank(certNo)) {
			throw new InvalidationException("인증번호를 입력하세요.");
		}

		// 인증번호 검사
		boolean same = sessionUtils.emailCert.check(bzmnDto.getEmailCertSessionId(), certNo);
		if (!same) {
			throw new InvalidationException("인증번호가 일치하지 않습니다.");
		}
	}

	/**
	 * 사업자 회원정보 변경
	 * @param param	변경된 사업자 회원정보
	 */
	public void change(ChangeParam param)
	{
		log.debug(param.toString());
		BnMember worker = checkWorkerIsChgBizTrgt();
		CmmtMberInfo cmmtMember = cmmtMemberDao.select(worker.getMemberId());
		if (cmmtMember == null) {
			throw new InvalidationException("회원정보가 없습니다.");
		}

		InvalidationsException ies = new InvalidationsException();

		if ( CodeExt.memberTypeExt.isIndividual(worker.getMemberType()) ) {
			if ( !string.equals(param.getMemberType(), CodeExt.memberTypeExt.개인사업자)
					&& !string.equals(param.getMemberType(), CodeExt.memberTypeExt.법인사업자) ) {
				ies.add("memberType", "개인사업자 또는 법인사업자 중 선택하세요.");
			}
		}
		else {
			if ( !string.equals(param.getMemberType(), CodeExt.memberTypeExt.법인사업자) ) {
				ies.add("memberType", "법인사업자로만 변경할 수 있습니다.");
			}
		}

		if (string.isBlank(param.getMemberNm())) {
			ies.add("memberNm", "사업자명을 입력하세요.");
		}

		if (string.isBlank(param.getCeoNm())) {
			ies.add("ceoNm", "대표자명을 입력하세요.");
		}

    	// 개인사업자가 아닌 경우 법인번호 필수 체크
    	if (!string.equals(param.getMemberType(), CodeExt.memberTypeExt.개인사업자)) {
        	if (string.isBlank(param.getJurirno())) {
        		ies.add("jurirno", String.format(validateMessageExt.입력없음, "법인등록번호"));
        	}
        	if (!CoreUtils.validation.isBubinNumber(param.getJurirno())) {
        		ies.add("jurirno", "법인번호가 올바르지 않습니다.");
    		}
    	}

		if (string.isBlank(param.getChargerNm())) {
			ies.add("chargerNm", "담당자명을 입력하세요.");
		}

		if (ies.size() > 0) {
			throw ies;
		}

		// 사업자전환 세션ID 모음 조회
		SessionIdSetForBzmnDto bzmnDto = getBzmnDto(param.getSessionId(), worker);

		// 사업자번호 Key로 사업자번호 조회
		PkiResult result = fwSessionUtils.pkiCertSession.get(bzmnDto.getPkiCertSessionId());
		String ci = result.getIdentifyData();	// 본인식별번호
		String bizrno = result.getBizrno();		// 사업자등록번호

		// 휴대폰번호 Key로 휴대폰 번호 조회
		if (string.isBlank(bzmnDto.getPhoneCertSessionId())) {
			throw new InvalidationException("휴대폰 인증을 완료하세요.");
		}
		String mobileNo = sessionUtils.mobileSmsNoCert.getMobileNo(bzmnDto.getPhoneCertSessionId());

		// 이메일 key로 이메일 조회
		if (string.isBlank(bzmnDto.getEmailCertSessionId())) {
			throw new InvalidationException("이메일 인증을 완료하세요.");
		}
		String email = sessionUtils.emailCert.getEmail(bzmnDto.getEmailCertSessionId());

		/*
		 * 회원정보 수정
		 */
		Date now = new Date();

		String encMobileNo = CoreUtils.aes256.encrypt(mobileNo, cmmtMember.getMemberId());
		String encEmail = CoreUtils.aes256.encrypt(email, cmmtMember.getMemberId());

		cmmtMember.setMemberType(param.getMemberType());
		cmmtMember.setMemberNm(param.getMemberNm());
		cmmtMember.setCeoNm(param.getCeoNm());
		cmmtMember.setJurirno(param.getJurirno());
		cmmtMember.setBizrno(bizrno); // 사업자등록번호
		cmmtMember.setCi(ci); // 공동인증서 CI
		cmmtMember.setChargerNm(param.getChargerNm());
		cmmtMember.setEncMobileNo(encMobileNo);
		cmmtMember.setEncEmail(encEmail);
		cmmtMember.setUpdatedDt(now);
		cmmtMember.setUpdaterId(worker.getMemberId());

		cmmtMemberDao.update(cmmtMember);

		// 개인회원인 경우 약관동의이력 입력
		if ( CodeExt.memberTypeExt.isIndividual(worker.getMemberType()) ) {
	        // 사업자 전환 약관동의정보 생성
	        termsUtils.insertList(bzmnDto.getTermsConsentSessionId(), worker.getMemberId());
		}

		// 인증 세션ID 만료 처리
		expiredSessionId(bzmnDto);
		if (string.isNotBlank(param.getSessionId())) {
			fwSessionUtils.expire(param.getSessionId());
		}
	}

	/**
	 * 공동인증서 인증결과 검증
	 *
	 * @param param 약관동의 및 공동인증서 인증결과 세션ID 모음
	 * @return 공동인증서 인증결과 신규저장 세션ID
	 */
	public SessionKeyDto bizrnoCert(PkiCertParam param)
	{
		BnMember worker = checkWorkerIsChgBizTrgt();

		CmmtMberInfo cmmtMember = cmmtMemberDao.select(worker.getMemberId());
		if (cmmtMember == null) {
			// 약관동의 임시저장 세션ID 만료 처리
			fwSessionUtils.expire(param.getTermsConsentSessionId());
			// 공동인증서 결과 세션ID 만료 처리
			fwSessionUtils.expire(param.getPkiCertSessionId());

			throw new InvalidationException("회원정보가 없습니다.");
		}

		// 공동인증서 인증결과 정보를 세션 테이블로부터 조회
		PkiResult pkiResult = fwSessionUtils.pkiCertSession.check(param.getPkiCertSessionId());

        CmmtMberInfo member = cmmtMemberDao.selectByCi(pkiResult.getIdentifyData());
		if (member != null) {
			// 약관동의 임시저장 세션ID 만료 처리
			fwSessionUtils.expire(param.getTermsConsentSessionId());
			// 공동인증서 결과 세션ID 만료 처리
			fwSessionUtils.expire(param.getPkiCertSessionId());

			throw new InvalidationException("동일 인증으로 가입된 사업자 회원이 있습니다.");
		}

		// 개인회원인 경우 약관동의 임시저장 정보를 세션 테이블로부터 조회하여 약관동의 세션 생성
		String termSessionId = "";
		if ( CodeExt.memberTypeExt.isIndividual(worker.getMemberType()) ) {
			List<SessionConsentDto> consentList = fwSessionUtils.termsConsentSession.get(param.getTermsConsentSessionId());
			if (consentList.isEmpty()) {
				// 약관동의 임시저장 세션ID 만료 처리
				fwSessionUtils.expire(param.getTermsConsentSessionId());
				// 공동인증서 결과 세션ID 만료 처리
				fwSessionUtils.expire(param.getPkiCertSessionId());

				throw new InvalidationException("사업자 전환 약관동의 정보가 없습니다.");
			}
			termSessionId = fwSessionUtils.termsConsentSession.set(consentList);
		}

		// 세션 새로 저장
		String pkiSessionId = fwSessionUtils.pkiCertSession.set(pkiResult);

		// 기존 정보 만료 처리
		fwSessionUtils.expire(param.getTermsConsentSessionId());
		fwSessionUtils.expire(param.getPkiCertSessionId());

		// 사업자전환 세션ID 모음 세션 저장
		SessionIdSetForBzmnDto bzmnDto = SessionIdSetForBzmnDto.builder()
											.termsConsentSessionId(termSessionId)
											.pkiCertSessionId(pkiSessionId)
											.build();
		String bzmnSessionId = sessionUtils.bzmnSessId.set(bzmnDto);

		return new SessionKeyDto(bzmnSessionId);
	}

}
