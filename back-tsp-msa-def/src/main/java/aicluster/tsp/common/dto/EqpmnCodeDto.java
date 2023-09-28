package aicluster.tsp.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "Code 그룹")
public class EqpmnCodeDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2896486353362290656L;
	@ApiModelProperty(value = "코드명")
	private String codeNm;
	@ApiModelProperty(value = "코드")
	private String code;
//	@ApiModelProperty(value = "코드그룸")
//	private String codeGroup;
//	@ApiModelProperty(value = "코드그룹명")
//	private String codeGroupNm;
}
