package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class FrontEvlOpinionListDto implements Serializable {
	private static final long serialVersionUID = -2499606332764300507L;
	/** 평가자명 */
	private String expertNm;
	/** 평가의견 */
	private String evlOpinion;
}
