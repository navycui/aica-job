package aicluster.tsp.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "견적신청")
public class FrontEqpmnSelectListDto implements Serializable {
	private static final long serialVersionUID = -2603157399188424720L;

	@ApiModelProperty(value = "장비ID")
	private String eqpmnId;
	@ApiModelProperty(value = "장비명(국문)")
	private String eqpmnNmKorean;
	@ApiModelProperty(value = "장비명(국문)")
	private String eqpmnNmEngl;
	@ApiModelProperty(value = "모델명")
	private String modelNm;
	@ApiModelProperty(value = "1시간 사용료")
	private Integer rntfeeHour;
	@ApiModelProperty(value = "이미지ID")
	private String imageId;
	@ApiModelProperty(value = "장비 분류명")
	private String eqpmnClNm;

	private List<String> eqpmnClList;
	public List<String> getEqpmnClList() {
		List<String> eqpmnClList = new ArrayList<>();
		if (this.eqpmnClList != null) {
			eqpmnClList.addAll(this.eqpmnClList);
		}
		return eqpmnClList;
	}
	public void setEqpmnClList(List<String> eqpmnClList) {
		this.eqpmnClList = new ArrayList<>();
		if (eqpmnClList != null) {
			this.eqpmnClList.addAll(eqpmnClList);
		}
	}
}