package aicluster.member.api.member.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PkiCertParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1808219574356358665L;

	private String pkiCertSessionId;		/** 공동인증서 인증결과정보 세션ID */
	private String termsConsentSessionId;	/** 약관동의정보 세션ID */
}
