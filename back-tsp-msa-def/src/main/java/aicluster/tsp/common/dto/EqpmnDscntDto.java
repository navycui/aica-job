package aicluster.tsp.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "할인 전체 리스트")
public class EqpmnDscntDto implements Serializable {

    private static final long serialVersionUID = -7570669433610772423L;
    @ApiModelProperty(value = "할인ID")
    private String dscnt_id;


}
