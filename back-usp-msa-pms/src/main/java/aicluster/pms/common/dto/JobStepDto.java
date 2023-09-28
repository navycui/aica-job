package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class JobStepDto implements Serializable {
	private static final long serialVersionUID = -4977450700795910964L;
	/** 업무단계 */
	private String jobStepCd;
	/** 업무단계명 */
	private String jobStepNm;
	/** 선택여부 */
	private Boolean isChecked;

}
