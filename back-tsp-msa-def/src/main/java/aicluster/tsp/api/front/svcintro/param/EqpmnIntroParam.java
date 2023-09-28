package aicluster.tsp.api.front.svcintro.param;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EqpmnIntroParam implements Serializable {
    private static final long serialVersionUID = 3752843925194054386L;

    @ApiModelProperty(value = "장비ID")
    private String eqpmnId;
    @ApiModelProperty(value = "장비명(국문)")
    private String eqpmnNmKorean;
    @ApiModelProperty(value = "장비명(영문)")
    private String eqpnmNmEngl;
    @ApiModelProperty(value = "이미지ID")
    private String imageId;
    @ApiModelProperty(value = "대분류명")
    private String eqpmnClNm;
    @ApiModelProperty(value = "소분류명")
    private String eqpmnSmNm;
    @ApiModelProperty(value = "분류ID")
    private String eqpmnClId;
    @ApiModelProperty(value = "모델명")
    private String modelNm;

}
