package aicluster.framework.common.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import aicluster.framework.common.dao.FwStplatAgreDtlsDao;
import aicluster.framework.common.dao.FwStplatDao;
import aicluster.framework.common.dto.SessionConsentDto;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtStplat;
import aicluster.framework.common.entity.CmmtStplatAgreDtls;
import aicluster.framework.security.Code;
import aicluster.framework.security.Code.validateMessage;
import aicluster.framework.security.SecurityUtils;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.array;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.property;
import bnet.library.util.CoreUtils.string;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TermsUtils {

	@Autowired
	private FwStplatAgreDtlsDao termsConsentDao;

	@Autowired
	private FwStplatDao termsDao;

	@Autowired
	private SessionUtils sessionUtils;

	/**
	 * 약관유형 배열(arrTermsType)에 정의된 약관 유형들이
	 * session 테이블의 저장된 Object에 약관동의여부 객체로 정의되어 있는지 검증한다.
	 * (약관유형에 동의여부가 입력되지 않았다면 false로 리턴한다.)
	 *
	 * @param sessionId 약관동의정보를 저장한 SESSION ID
	 * @param arrTermsType 약관동의정보에 입력되어 있어야하는 약관유형코드(G:TERMS_TYPE) 배열
	 * @return boolean (true/false)
	 */
	public boolean isChkInputTerms(String sessionId, String[] arrTermsType)
	{
		if (string.isBlank(sessionId)) {
			log.error("수행한 약관 동의가 없습니다.");
			System.out.println("수행한 약관 동의가 없습니다.");
			return false;
		}

		if (ArrayUtils.isEmpty(arrTermsType)) {
			log.error("기준 약관유형코드가 정의되지 않았습니다.");
			System.out.println("기준 약관유형코드가 정의되지 않았습니다.");
			return false;
		}

		// CMMT_SESSION으로부터 약관동의정보 조회
		List<SessionConsentDto> consentList = sessionUtils.termsConsentSession.get(sessionId);

		// 현재일자에 유효한 약관정보 조회
		List<CmmtStplat> termsList = new ArrayList<>();
		for (String termsType : arrTermsType) {
			termsList.addAll(termsDao.select_today(termsType));
		}

		if (termsList.isEmpty()) {
			log.error("기준 약관정보가 없습니다.");
			System.out.println("기준 약관정보가 없습니다.");
			return false;
		}

		if ( termsList.size() != consentList.size() ) {
			log.error(String.format("기준 약관정보 건 수와 약관동의 받은 건 수가 다릅니다.[%d]/[%d]", termsList.size(), consentList.size()));
			System.out.println(String.format("기준 약관정보 건 수와 약관동의 받은 건 수가 다릅니다.[%d]/[%d]", termsList.size(), consentList.size()));
			return false;
		}

		// 약관동의정보에 약관정보가 정의되어 있는지 검증
		boolean isTerms = true;
		for (CmmtStplat terms : termsList) {
			isTerms = consentList.stream()
					.anyMatch(consent -> string.equals(consent.getTermsType(), terms.getTermsType())
							&& string.equals(consent.getBeginDay(), terms.getBeginDay())
							&& consent.isRequired() == terms.isRequired());

			if (BooleanUtils.isNotTrue(isTerms)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 사용자가 로그인한 상태에서 세션 테이블에 저장된
	 * 약관동의정보를 CMMT_TERMS_CONSENT에 저장한다.
	 * (약관동의이력 생성 전에 동일 약관유형에 대한 동의이력을 삭제한다.)
	 *
	 * @param sessionId : 약관동의정보를 저장한 SESSION ID
	 */
	public void insertList(String sessionId)
	{
		insertList(sessionId, null);
	}

	/**
	 * 회원가입 등 로그인 되지 않은 상태에서 회원ID 정의하여
	 * 세션 테이블에 저장된 약관 동의정보를 CMMT_TERMS_CONSENT에 저장한다.
	 * (약관동의이력 생성 전에 동일 약관유형에 대한 동의이력을 삭제한다.)
	 *
	 * @param sessionId : 약관동의정보를 저장한 SESSION ID
	 * @param memberId : 회원ID
	 */
	public void insertList(String sessionId, String memberId)
	{
		if (string.isBlank(sessionId)) {
			throw new InvalidationException("수행한 약관 동의가 없습니다.");
		}

		// CMMT_SESSION으로부터 약관동의정보 조회
		List<SessionConsentDto> consentList = sessionUtils.termsConsentSession.get(sessionId);

		// cmmtTermsConsent entity로 Data 생성
		List<CmmtStplatAgreDtls> insertList = new ArrayList<>();
		for (SessionConsentDto consent : consentList) {
			CmmtStplatAgreDtls termsConsent = new CmmtStplatAgreDtls();
			property.copyProperties(termsConsent, consent);
			insertList.add(termsConsent);
		}

		// 입력 로직 수행
		insertList(insertList, memberId);
	}

	/**
	 * 사용자가 로그인한 상태에서 약관동의정보를 CMMT_TERMS_CONSENT에 저장한다.
	 * (약관동의이력 생성 전에 동일 약관유형에 대한 동의이력을 삭제한다.)
	 *
	 * @param consentList : 약관동의정보 목록
	 */
	public void insertList(List<CmmtStplatAgreDtls> consentList) {
		insertList(consentList, null);
	}

	/**
	 * 회원가입 등 로그인 되지 않은 상태에서 회원ID 정의하여
	 * 약관동의정보를 CMMT_TERMS_CONSENT에 저장한다.
	 * (회원ID가 Null인 경우 JWT에 저장된 회원ID를 읽어들인다.)
	 * (약관동의이력 생성 전에 동일 약관유형에 대한 동의이력을 삭제한다.)
	 *
	 * @param consentList : 약관동의정보 목록
	 * @param memberId : 회원ID
	 */
	public void insertList(List<CmmtStplatAgreDtls> consentList, String memberId)
	{
		// 회원ID가 없는 경우 로그인한 사용자의 회원ID로 세팅
		if (string.isBlank(memberId)) {
			BnMember worker = SecurityUtils.checkLogin();
			memberId = worker.getMemberId();
		}

		// 약관동의정보 추가 값 세팅(회원ID, 보유만료일)
		for (CmmtStplatAgreDtls consent:consentList) {
			CmmtStplat terms = termsDao.select(consent.getTermsType(), consent.getBeginDay(), consent.isRequired());

			// 필수약관 동의여부 검증
			if (terms.isRequired() && !consent.isConsentYn()) {
				throw new InvalidationException(String.format("%s는 필수 동의해야합니다.", terms.getTermsTypeNm()));
			}

			// 보유만료일자 정의(동의일시(CmmtTermsConsent.consentDt) + 약관보유기간코드(CmmtTerms.possessTermCd))
			String[] arrNotExpiredCd = {Code.possessTerm.회원탈퇴, Code.possessTerm.준영구};
			if ( string.isBlank(terms.getPossessTermCd())
					|| !string.isNumeric(terms.getPossessTermCd())
					|| array.contains(arrNotExpiredCd, terms.getPossessTermCd()) ) {
				// 회원탈퇴 또는 준영구인 경우 보유만료일을 '9999-12-31'로 세팅한다.
				consent.setPossessExpiredDay("99991231");
			}
			else {
				String possesExpiredDay = date.format(date.addYears(consent.getConsentDt(), string.toInt(terms.getPossessTermCd())), "yyyyMMdd");
				consent.setPossessExpiredDay(possesExpiredDay);
			}

			// 약관동의 회원ID 세팅
			consent.setMemberId(memberId);

			// 기존 약관동의이력 삭제 처리
			List<CmmtStplatAgreDtls> oldConsentList = selectList(consent.getTermsType(), null, null, consent.getMemberId());
			if (!oldConsentList.isEmpty()) {
				termsConsentDao.delete(consent.getTermsType(), consent.getMemberId());
			}
		}

		// 약관동의정보 저장
		termsConsentDao.insertList(consentList);
	}

	/**
	 * 약관유형에 해당하는 약관동의정보 목록 조회
	 *
	 * @param termsType 약관유형(G: TERMS_TYPE)
	 * @return 약관동의정보 목록
	 */
	public List<CmmtStplatAgreDtls> selectList(String termsType)
	{
		return selectList(termsType, null, null, null);
	}

	/**
	 * 약관정보 PK 컬럼으로 약관동의정보 목록 조회
	 *
	 * @param termsType : 약관유형(G: TERMS_TYPE)
	 * @param beginDay : 시작일(시행일)
	 * @param required : 필수여부(true:필수, false:선택)
	 * @return 약관동의정보
	 */
	public List<CmmtStplatAgreDtls> selectList(String termsType, String beginDay, boolean required)
	{
		return selectList(termsType, beginDay, required, null);
	}

	/**
	 * 약관동의정보 목록 조회
	 *
	 * @param termsType : [필수] 약관유형(G: TERMS_TYPE)
	 * @param beginDay : [옵션] 시작일(시행일)
	 * @param required : [옵션] 필수여부(true:필수, false:선택)
	 * @param memberId : [옵션] 회원ID
	 * @return 약관동의정보 목록
	 */
	public List<CmmtStplatAgreDtls> selectList(String termsType, String beginDay, Boolean required, String memberId)
	{
		return termsConsentDao.selectList(termsType, beginDay, required, memberId);
	}

	/**
	 * 만료일자가 다가오는 약관동의정보 목록 조회
	 *
	 * @param expiredCalType : 만료계산유형(DAY:일단위, MONTH:월단위)
	 * @param expiredCalVal : 만료계산값(숫자)
	 * @param termsType : 약관유형(G: TERMS_TYPE)
	 * @param beginDay : 시작일(시행일)
	 * @param required : 필수여부(true:필수, false:선택)
	 * @return 약관동의정보 목록
	 */
	public List<CmmtStplatAgreDtls> selectExpiredList(String expiredCalType, int expiredCalVal, String termsType, String beginDay, boolean required)
	{
		if ( string.isNotBlank(expiredCalType) ) {
			String[] arrExpiredCalType = {Code.expiredCalType.일단위, Code.expiredCalType.월단위};
			if ( !ArrayUtils.contains(arrExpiredCalType, expiredCalType) ) {
				throw new InvalidationException(String.format(validateMessage.유효오류, "만료일 계산 유형"));
			}
		}

		return termsConsentDao.selectExpiredList(expiredCalType, expiredCalVal, termsType, beginDay, required);
	}
}
