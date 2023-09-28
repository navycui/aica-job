package aicluster.tsp.common.dto;

import aicluster.tsp.common.util.TspCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "통계 상태")
public class StatisticsDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2896486353362290656L;

	@ApiModelProperty(value = "타이틀")
	public String title;
	@ApiModelProperty(value = "비율")
	public Double rate;

}
