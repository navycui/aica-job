package aicluster.tsp.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "사용료 관련")
public class EqpmnRntfeeDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4206469873615590349L;
	@ApiModelProperty(value = "1시간 사용료")
	private Integer rntfeeHour;
	@ApiModelProperty(value = "1일 가용시작시간")
	private Integer usefulBeginHour;
	@ApiModelProperty(value = "1일 가용종료시간")
	private Integer usefulEndHour;
	@ApiModelProperty(value = "법정공휴일 휴일포함")
	private Boolean hldyInclsAt;
	@ApiModelProperty(value = "반출시 휴일포함")
	private Boolean tkoutHldyInclsAt;
	@ApiModelProperty(value = "미반출시 휴일포함")
	private Boolean nttkoutHldyInclsAt;
}

