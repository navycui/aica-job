package aicluster.pms.common.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class EvlPlanPblancStepDto implements Serializable {
	private static final long serialVersionUID = 1860582018704707995L;

	/** 공고ID */
	@JsonIgnore
	private String pblancId;
	/** 접수차수 */
	@JsonIgnore
	private Integer rceptOdr;
	/** 평가계획ID */
	@JsonIgnore
	private String evlPlanId;
	/** 평가단계ID */
	private String evlStepId;
	/** 평가단계명 */
	private String  evlStepNm;
	/** 선정대상수 */
	Integer slctnTargetCo;
	/** 분과ID */
	private String sectId;
	/** 분과명 */
	private String sectNm;
	/** 최종선정대상ID */
	private String evlLastSlctnId;
}
