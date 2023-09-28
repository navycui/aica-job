package aicluster.pms.api.selection.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class LastSelectionParam implements Serializable {
	private static final long serialVersionUID = 3505549768146420888L;
	/** 신청ID */
	private String applyId;
	/** 선정여부 ('Y':선정, 'N':선정취소) */
	private String slctnAt;
	/** 총사업비 */
	private Long totalWct;
	/** 지원예산 */
	private Long sportBudget;
	/** 부담금현금 */
	private Long alotmCash;
	/** 부담금현물 */
	private Long alotmActhng;
}
