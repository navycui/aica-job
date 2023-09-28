package aicluster.framework.common.util.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PkiResult implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1604200914653776736L;

	private String signerDn;			/** 사용자 DN */
	private String issuerDn;			/** 발급자 DN */
	private String serialNumber;		/** 인증서 SN */
	private String policyIdentifier;	/** 인증서 정책 */
	private String policy;				/** 인증서 구분 */
	private String identifyData;		/** 본인확인 식별값 */
	private String bizrno;				/** 사업자등록번호 */
}
