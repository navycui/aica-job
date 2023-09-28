package aicluster.tsp.api.admin.eqpmn.mngm.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "장비정보 관리정보")
public class MngmParam implements Serializable {

	private static final long serialVersionUID = -5987794279854777972L;
		@ApiModelProperty(value = "장비ID")
		private String eqpmnId;
		@ApiModelProperty(value = "점검대상여부")
		private boolean chckTrgetAt;
		@ApiModelProperty(value = "시간당 사용료")
		private Integer rntfeeHour;
		@ApiModelProperty(value = "1일 가용시작시간")
		private Integer usefulBeginHour;
		@ApiModelProperty(value = "1일 가용종료시간")
		private Integer usefulEndHour;
		@ApiModelProperty(value = "사용율 저조설정")
		private boolean useRateInctvSetupAt;
		@ApiModelProperty(value = "법정공휴일 휴일포함")
		private boolean hldyInclsAt;
		@ApiModelProperty(value = "반출시 휴일포함")
		private boolean tkoutHldyInclsAt;
		@ApiModelProperty(value = "미반출시 휴일포함")
		private boolean nttkoutHldyInclsAt;
		@ApiModelProperty(value = "교정대상여부")
		private boolean crrcTrgetAt;
		@ApiModelProperty(value = "교정주기(일)")
		private Integer crrcCycle;
}