package aicluster.tsp.api.common.param;

import aicluster.tsp.common.dto.EqpmnCodeDto;
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
@ApiModel(description = "공통코드")
public class CommonCodeGroup  implements Serializable {
    private static final long serialVersionUID = -5949910601626561807L;
    @ApiModelProperty(value = "공통코드")
    private List<EqpmnCodeDto> codeGroup;

    public List<EqpmnCodeDto> getCodeGroup() {
        List<EqpmnCodeDto> codeGroup = new ArrayList<>();
        if (this.codeGroup != null) {
            codeGroup.addAll(this.codeGroup);
        }
        return codeGroup;
    }

    public void setCodeGroup(List<EqpmnCodeDto> codeGroup) {
        this.codeGroup = new ArrayList<>();
        if (codeGroup != null) {
            this.codeGroup.addAll(codeGroup);
        }
    }
}