package aicluster.tsp.api.common.param;

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
@ApiModel(description = "put, post용 공용 param")
public class CommonRequestParam implements Serializable {
    private static final long serialVersionUID = -8469218308958815169L;

    @ApiModelProperty(value = "param")
    private String param;

    public void setParam(String param) {
        this.param = param;
    }

    public String getParam() {
        return param;
    }

}
