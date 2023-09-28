package aicluster.tsp.api.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "계산.")
public class CommonCalcParam implements Serializable {
	private static final long serialVersionUID = 3140497070226750689L;
	@ApiModelProperty(value = "1시간 사용료")
	private Integer rntfeeHour;
	@ApiModelProperty(value = "1일 가용시작시간")
	private Integer usefulBeginHour;
	@ApiModelProperty(value = "1일 가용종료시간")
	private Integer usefulEndHour;
	@ApiModelProperty(value = "1일 가용시간")
	private Integer usefulHour;
	@ApiModelProperty(value = "예상시간")
	private Integer totalHrs;
	@ApiModelProperty(value = "예상가격")
	private Integer rntfee;
	@ApiModelProperty(value = "총 사용분")
	private Integer totalMin;
}