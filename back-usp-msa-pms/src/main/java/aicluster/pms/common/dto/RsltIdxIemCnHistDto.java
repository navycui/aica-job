package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class RsltIdxIemCnHistDto implements Serializable {
	private static final long serialVersionUID = 7759441969041748099L;
	/** 성과이력ID */
	private String rsltHistId;
	/** 성과지표항목내용ID */
	private String rsltIdxIemCnId;
	/** 성과지표세부항목ID */
	private String rsltIdxDetailIemId;
	/** 세부항목명 */
	private String detailIemNm;
	/** 성과지표기준항목ID */
	private String rsltIdxStdIemId;
	/** 기준항목명 */
	private String stdIemNm;
	/** 항목단위코드(G:IEM_UNIT) */
	private String iemUnitCd;
	/** 성과지표항목내용 */
	private String rsltIdxIemCn;
	/** 정렬순서 */
	private Integer sortOrder;
}
