package aicluster.tsp.api.admin.eqpmn.reprt.param;

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
@ApiModel(description = "사용신청 처리")
public class ReprtProcessParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2478294785387422672L;
	@ApiModelProperty(value = "장비견적신청ID")
	private String reprtId;
	@ApiModelProperty(value = "신청 상태")
	private String reprtSttus;
	@ApiModelProperty(value = "사유")
	private String rsndqf;
	@ApiModelProperty(value = "수정자")
	private String updusrId;

}