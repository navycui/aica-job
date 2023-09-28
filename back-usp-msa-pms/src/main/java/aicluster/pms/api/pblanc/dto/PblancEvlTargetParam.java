package aicluster.pms.api.pblanc.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class PblancEvlTargetParam implements Serializable {
	private static final long serialVersionUID = -3604158656316452321L;
	/** 평가단계ID */
	private String evlStepId;
	/** 분과ID */
	private String sectId;
	/** 최종선정대상ID */
	private String evlLastSlctnId;

	private Long beginRowNum;
	private Long itemsPerPage;
}
