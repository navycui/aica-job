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
@ApiModel(description = "입금 안내문")
public class UseRcpmnyGdccParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4628888790065302307L;
	@ApiModelProperty(value = "장비견적신청ID")
	private String reqstId;
	@ApiModelProperty(value = "입금 안내문 내용")
	private String rcpmnyGdcc;

	@ApiModelProperty(value = "수정자")
	private String updusrId;

}