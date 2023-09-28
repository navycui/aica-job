package aicluster.member.api.module.service;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aicluster.framework.common.dto.SessionKeyDto;
import aicluster.framework.common.util.SessionUtils;
import aicluster.framework.common.util.dto.PkiResult;
import aicluster.member.api.module.dto.PkiResultParam;
import bnet.library.exception.InvalidationException;
import bnet.library.util.CoreUtils.string;

@Service
public class PkiCertService {

	@Autowired
	private SessionUtils fwSessionUtils;

	public SessionKeyDto saveSigned(PkiResultParam param)
	{
		if (BooleanUtils.isNotTrue(param.getIsSigned())) {
			throw new InvalidationException("전자서명 원문이 일치하지 않습니다. 다시 수행하세요.");
		}

		if (!string.startsWith(param.getPolicy(), "범용") || !string.contains(param.getPolicy(), "법인")) {
			throw new InvalidationException("법용법인 인증서만 인증할 수 있습니다.");
		}

		if (string.isBlank(param.getBizrno())) {
			throw new InvalidationException("사업자등록번호가 없습니다.");
		}

		PkiResult result = PkiResult.builder()
								.signerDn(param.getSignerDn())
								.issuerDn(param.getIssuerDn())
								.serialNumber(param.getSerialNumber())
								.policyIdentifier(param.getPolicyIdentifier())
								.policy(param.getPolicy())
								.identifyData(param.getIdentifyData())
								.bizrno(param.getBizrno())
								.build();

		String sessionId = fwSessionUtils.pkiCertSession.set(result);

		return new SessionKeyDto(sessionId);
	}
}
