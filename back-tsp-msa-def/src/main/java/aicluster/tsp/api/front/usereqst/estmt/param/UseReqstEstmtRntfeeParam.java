package aicluster.tsp.api.front.usereqst.estmt.param;

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
@ApiModel(description = "예상 사용금액")
public class UseReqstEstmtRntfeeParam implements Serializable {
    private static final long serialVersionUID = -4495318390195180142L;

    @ApiModelProperty(value = "1일 가용시간")
    private Integer usefulHour;

    @ApiModelProperty(value = "총 사용시간")
    private Integer expectUsgtm;

    @ApiModelProperty(value = "1시간 사용료")
    private Integer rntfeeHour;

    @ApiModelProperty(value = "예상 사용금액")
    private Integer expectRntfee;

}
