package aicluster.tsp.api.admin.eqpmn.use.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "사용관리 추가금액 등록")
public class UseAditRntfeeParam implements Serializable {
	/**
	 * 
	 */
	@ApiModelProperty(value = "장비견적신청ID")
	private String reqstId;
	@ApiModelProperty(value = "청구 금액")
	private Integer aditRntfee;
	@ApiModelProperty(value = "청구사유")
	private String rqestResn;
	@ApiModelProperty(value = "수정자")
	private String updusrId;
}