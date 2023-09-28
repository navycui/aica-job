package aicluster.tsp.api.common.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@ApiModel(description = "장비사용 상태변경")
public class CommonEqpmnUseSttusParam implements Serializable {
	private static final long serialVersionUID = 3140497070226750689L;
	@ApiModelProperty(value = "신청 Id")
	private String reqstId;
	@ApiModelProperty(value = "수정자ID", hidden = true)
	private String updusrId;
	@ApiModelProperty(value = "사용상태")
	private String useSttus;

}