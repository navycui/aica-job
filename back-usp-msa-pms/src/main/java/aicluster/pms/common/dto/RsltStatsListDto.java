package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class RsltStatsListDto implements Serializable {
	private static final long serialVersionUID = 4757239889130994157L;
	/** 사용자명 */
	private String memberNm;
	/** 성과항목명 */
	private String rsltIemNm;
	/** 성과지표항목내용 */
	private String rsltIdxIemCn;
	/** 항목단위코드 */
	private String iemUnitCd;
}
