package aicluster.tsp.api.front.usereqst.estmt.param;

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
@ApiModel(description = "견적신청")
public class UseReqstEstmtSelectParam implements Serializable {
    private static final long serialVersionUID = -4495318390195180142L;

    @ApiModelProperty(value = "장비명(국문)")
    private String eqpmnNmKorean;
    @ApiModelProperty(value = "장비분류")
    private String eqpmnClId;

}
