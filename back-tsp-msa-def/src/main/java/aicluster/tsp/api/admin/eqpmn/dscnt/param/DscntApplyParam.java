package aicluster.tsp.api.admin.eqpmn.dscnt.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DscntApplyParam implements Serializable {

    private static final long serialVersionUID = -3627980025704991265L;

    @ApiModelProperty(value = "할인 ID")
    private String dscntId;
    @ApiModelProperty(value = "장비 ID")
    private String eqpmnId;
    @ApiModelProperty(value = "생성자ID")
    private String creatrId;
    @ApiModelProperty(value = "생성일시")
    private Date creatDt;
    @ApiModelProperty(value = "수정자ID")
    private String updusrId;
    @ApiModelProperty(value = "수정일시")
    private Date updtDt;
    public Date getCreatDt() {
        if (this.creatDt != null) {
            return new Date(this.creatDt.getTime());
        }
        return null;
    }
    public void setCreatDt(Date creatDt) {
        this.creatDt = null;
        if (creatDt != null) {
            this.creatDt = new Date(creatDt.getTime());
        }
    }
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