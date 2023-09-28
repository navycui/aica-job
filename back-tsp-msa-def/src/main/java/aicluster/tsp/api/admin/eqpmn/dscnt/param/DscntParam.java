package aicluster.tsp.api.admin.eqpmn.dscnt.param;

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
public class DscntParam implements Serializable {

    private static final long serialVersionUID = 28913304948846685L;

    @ApiModelProperty(value = "할인 ID")
    private String dscntId;
    @ApiModelProperty(value = "할인사유")
    private String dscntResn;
    @ApiModelProperty(value = "할인률(%)")
    private Integer dscntRate;
    @ApiModelProperty(value = "사용상태")
    private String useSttus;
    @ApiModelProperty(value = "생성자ID")
    private String creatrId;
    @ApiModelProperty(value = "수정자ID")
    private String updusrId;
    @ApiModelProperty(value = "수정일시")
    private Date updtDt;

    public Date getUpdtDt() {
        if (this.updtDt != null) {
            return new Date(this.updtDt.getTime());
        }
        return null;
    }
    public void setUpdtDt(Date updtDt) {
        this.updtDt = null;
        if (updtDt != null) {
            this.updtDt = new Date(updtDt.getTime());
        }
    }

}
