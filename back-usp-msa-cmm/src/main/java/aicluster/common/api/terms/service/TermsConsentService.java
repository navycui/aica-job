package aicluster.common.api.terms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.common.api.terms.dto.TermsConsentParam;
import aicluster.common.common.dao.CmmtStplatDao;
import aicluster.common.common.entity.CmmtStplat;
import aicluster.framework.common.dto.SessionConsentDto;
import aicluster.framework.common.dto.SessionKeyDto;
import aicluster.framework.common.util.SessionUtils;
import aicluster.framework.security.Code.validateMessage;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.property;
import bnet.library.util.CoreUtils.string;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TermsConsentService {

	@Autowired
	private CmmtStplatDao termsDao;

	@Autowired
	private SessionUtils sessionUtils;

	/**
	 * 약관동의정보를 생성하여 세션 테이블에 저장한다.
	 *
	 * @param param : 약관동의 Parameter(termsType:약관유형, beginDay:시행일(시작일), consentYn:동의여부)
	 * @return SessionKeyDto : 약관동의정보가 저장된 SESSION ID
	 */
	public SessionKeyDto saveSession(List<TermsConsentParam> param)
	{
		Date now = new Date();

		if (param.isEmpty()) {
			throw new InvalidationException("약관 동의을 수행하세요.");
		}

		List<SessionConsentDto> list = new ArrayList<>();
		for(TermsConsentParam consentParam:param) {
			CmmtStplat terms = termsDao.select(consentParam.getTermsType(), consentParam.getBeginDay(), consentParam.isRequired());
			if (terms == null) {
				throw new InvalidationException(String.format(validateMessage.유효오류, "약관정보"));
			}

			// 약관 항목 중복 입력 검증
			int dupCnt = param.stream()
					.filter(obj -> string.equals(obj.getTermsType(), consentParam.getTermsType())
							&& string.equals(obj.getBeginDay(), consentParam.getBeginDay())
							&& obj.isRequired() == consentParam.isRequired())
					.toArray().length;

			// 1건 초과이면 중복으로 판단.
			if (dupCnt > 1) {
				Object[] arrConsent = param.stream()
						.filter(obj -> string.equals(obj.getTermsType(), consentParam.getTermsType())
								&& string.equals(obj.getBeginDay(), consentParam.getBeginDay())
								&& obj.isRequired() == consentParam.isRequired())
						.toArray();
				for (Object obj:arrConsent) {
					log.debug(((TermsConsentParam)obj).toString());
				}

				String sTemrsTypeNm = terms.getTermsTypeNm();
				if (terms.isRequired()) {
					sTemrsTypeNm += "-필수";
				}
				else {
					sTemrsTypeNm += "-선택";
				}
				throw new InvalidationException(String.format("[%s] 약관 항목이 중복 입력되었습니다.", sTemrsTypeNm));
			}

			// 필수약관 동의여부 검증
			if (terms.isRequired()) {
				if (BooleanUtils.isNotTrue(consentParam.getConsentYn())) {
					throw new InvalidationException(String.format("%s는 필수 동의해야합니다.", terms.getTermsTypeNm()));
				}
			}
			else {
				// 선택 약관의 경우 동의여부 입력값이 NULL이면 false로 정의
				if ( BooleanUtils.isNotTrue(consentParam.getConsentYn()) ) {
					consentParam.setConsentYn(false);
				}
			}

			// 약관동의정보 생성
			SessionConsentDto sessConsent = new SessionConsentDto();
			property.copyProperties(sessConsent, consentParam);
			sessConsent.setConsentDt(now);

			list.add(sessConsent);
		}

		// CMMT_SESSION 테이블에 데이터 등록
		String sessionId = sessionUtils.termsConsentSession.set(list);

		return new SessionKeyDto(sessionId);
	}

}
