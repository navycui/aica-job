package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class IoeSetupTaxitmDto implements Serializable {
	private static final long serialVersionUID = 663631185648051039L;
	/** 사업비세목ID */
	private String wctTaxitmId;
	/** 사업비세목명 */
	private String wctTaxitmNm;
	/** 선택여부 */
	private Boolean isChecked;

}
