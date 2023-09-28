package aicluster.member.api.module.dto;

import java.io.Serializable;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PkiResultParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4621059152541738735L;

	private String signData;			/** SignData */
	private String vidData;				/** VIDData */
	private String signerDn;			/** 사용자 DN */
	private String issuerDn;			/** 발급자 DN */
	private String serialNumber;		/** 인증서 SN */
	private String policyIdentifier;	/** 인증서 정책 */
	private String policy;				/** 인증서 구분 */
	private String identifyData;		/** 본인확인 식별값 */
	private String encBizrno;			/** 암호화된 사업자등록번호(key:본인확인 식별값) */
	private Boolean isSigned;			/** 전자서명 원문 일치여부 */

	public String getBizrno() {
		if (string.isBlank(this.encBizrno)) {
			return null;
		}
		return aes256.decrypt(this.encBizrno, this.identifyData);
	}
}
