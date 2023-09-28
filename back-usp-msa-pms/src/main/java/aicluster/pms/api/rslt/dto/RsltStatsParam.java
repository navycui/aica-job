package aicluster.pms.api.rslt.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class RsltStatsParam implements Serializable {
	private static final long serialVersionUID = 1294467545963270573L;
	/** 사업연도 */
	private String bsnsYear;
	/** 사업코드 */
	private String bsnsCd;
	/** 사업시작일  */
	private String bsnsBgnde;
	/** 사업종료일  */
	private String bsnsEndde;
	/** 사업자명 */
	private String memberNm;
	/** 로그인 내부사용자ID */
	private String insiderId;
}
