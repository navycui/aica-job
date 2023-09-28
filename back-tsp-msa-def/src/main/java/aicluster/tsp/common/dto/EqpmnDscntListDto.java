package aicluster.tsp.common.dto;


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
@ApiModel(description = "장비할인 목록 조회 리스트")
public class EqpmnDscntListDto implements Serializable {

    private static final long serialVersionUID = 3052069610333977091L;

    @ApiModelProperty(value = "할인ID")
    private String dscntId;
    @ApiModelProperty(value = "할인사유")
    private String dscntResn;
    @ApiModelProperty(value = "할인률(%)")
    private Integer dscntRate;
    @ApiModelProperty(value = "사용상태")
    private String useSttus;
}
