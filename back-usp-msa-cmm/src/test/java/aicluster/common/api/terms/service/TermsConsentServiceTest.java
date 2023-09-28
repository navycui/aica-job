package aicluster.common.api.terms.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import aicluster.common.api.terms.dto.TermsConsentParam;
import aicluster.common.common.entity.CmmtStplat;
import aicluster.common.support.TestServiceSupport;
import aicluster.framework.common.dto.SessionKeyDto;
import aicluster.framework.common.entity.BnMember;
import aicluster.framework.common.entity.CmmtStplatAgreDtls;
import aicluster.framework.common.util.TermsUtils;
import aicluster.framework.security.SecurityUtils;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.dto.JsonList;

public class TermsConsentServiceTest extends TestServiceSupport {
	@Autowired
	private TermsConsentService consentService;
	@Autowired
	private TermsService termsService;
	@Autowired
	private TermsUtils termsUtils;

	private final String[] TERMS_TYPES = {"TERMS_OF_USE", "PRVC_CLCT_AGRE_MBR"};	//약관동의 대상 약관유형코드 정의

	private List<CmmtStplat> termsList;
	private Map<String, String> termsBeginDay = new HashMap<String, String>();  //약관유형별 최근 시행일 Map

	@BeforeEach
	void init() {
		// 약관정보 목록 생성
		termsList = new ArrayList<>();
		for (String termsType : TERMS_TYPES) {
			JsonList<CmmtStplat> cmmtTermsList = termsService.getTodayTerms(termsType);
			termsBeginDay.put(termsType, cmmtTermsList.getList().get(0).getBeginDay());
			termsList.addAll(cmmtTermsList.getList());
		}
	}

	@Test
	void test() {

		// 약관동의정보 생성
		List<TermsConsentParam> consentParam = new ArrayList<>();
		for (CmmtStplat terms : termsList) {
			TermsConsentParam param = new TermsConsentParam();
			param.setTermsType(terms.getTermsType());
			param.setBeginDay(string.toDate(terms.getBeginDay()));
			param.setRequired(terms.isRequired());

			if (terms.isRequired()) {
				param.setConsentYn(true);
			}
			else {
				param.setConsentYn(false);
			}
			consentParam.add(param);
		}
		System.out.println(consentParam.toString());

		SessionKeyDto session = consentService.saveSession(consentParam);

		System.out.println(String.format("생성된 세션DTO : [%s]", session.toString()));
		assertNotNull(session.getKey());

		// SESSION 테이블에 저장된 약관동의정보에 대해서 대상이 되는 약관유형 정보가 포함되어 있는지 검증
		boolean isChkTermsType = termsUtils.isChkInputTerms(session.getKey(), new String[]{"TERMS_OF_USE", "PRVC_CLCT_AGRE_BIZ"});
		System.out.println( String.format("약관유형 포함 여부 검증결과(다른값) : [%s]", BooleanUtils.toStringTrueFalse(isChkTermsType)) );
		assertFalse(isChkTermsType);

		isChkTermsType = termsUtils.isChkInputTerms(session.getKey(), TERMS_TYPES);
		System.out.println( String.format("약관유형 포함 여부 검증결과(동일값) : [%s]", BooleanUtils.toStringTrueFalse(isChkTermsType)) );
		assertTrue(isChkTermsType);

		// SESSION 테이블에 저장된 약관동의정보를 CMMT_TERMS_CONSENT 테이블에 저장
		// 로그인상태이면 아래 메서드를 호출하고 로그인 상태가 아닌 경우(회원가입 등) 인 경우 termsUtils.insertList(session.getKey(), memberId)로 memberId를 별도로 생성하여 전달한다.
		termsUtils.insertList(session.getKey());

		// CMMT_TERMS_CONSENT 테이블에 저장된 약관동의정보 조회
		BnMember worker = SecurityUtils.getCurrentMember();
		List<CmmtStplatAgreDtls> consentList = new ArrayList<>();
		for (String termsType : TERMS_TYPES) {
			String beginDay = termsBeginDay.get(termsType);
			List<CmmtStplatAgreDtls> termsConsentSet = termsUtils.selectList(termsType, beginDay, null, worker.getMemberId());
			if (termsConsentSet != null) {
				consentList.addAll(termsConsentSet);
			}
		}
		System.out.println(String.format("저장된 약관동의정보 : [%s]", consentList.toString()));
		assertNotNull(consentList);
	}
}
