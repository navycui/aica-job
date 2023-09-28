package aicluster.tsp.api.front.svcintro.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EqpmnIntroSearchParam implements Serializable {
    private static final long serialVersionUID = -4168429711336661518L;

    @ApiModelProperty(value = "대분류")
    private String eqpmnClNm;
    @ApiModelProperty(value = "장비명")
    private String eqpmnNmKorean;
    @ApiModelProperty(value = "모델명")
    private String modelNm;
}
