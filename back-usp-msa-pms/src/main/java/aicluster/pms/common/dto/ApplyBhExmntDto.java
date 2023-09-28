package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ApplyBhExmntDto implements Serializable {
	private static final long serialVersionUID = 1335906702055830497L;
	/** 사전검토ID */
	private String bhExmntId;
	/** 사전검토명 */
	private String bhExmntDivNm;
	/** 사전검토항목ID */
	private String bhExmntIemId;
	/** 사전검토항목명 */
	private String bhExmntIemNm;
	/** 체크결과구분코드(G:CECK_RESULT_DIV) */
	private String ceckResultDivCd;
}
