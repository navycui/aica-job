package aicluster.tsp.api.admin.eqpmn.extnd.param;

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
@ApiModel(description = "기간연장신청 조회 리스트")
public class ExtndDetailProcessParam implements Serializable {
	private static final long serialVersionUID = -7240134340483841154L;

	@ApiModelProperty(value = "신청상태")
	private String reqstSttus;
	@ApiModelProperty(value = "사용시간")
	private Integer usgtm;
	@ApiModelProperty(value = "할인ID")
	private String dscntId;
	@ApiModelProperty(value = "사용료")
	private Integer rntfee;
	@ApiModelProperty(value = "할인금액")
	private Integer dscntAmount;
	@ApiModelProperty(value = "사유(보완,반려)")
	private String rsndqf;
	@ApiModelProperty(value = "미납처리 내용")
	private String npyResn;
	@ApiModelProperty(value = "입금안내문")
	private String rcpmnyGdcc;
}