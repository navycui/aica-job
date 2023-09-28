package aicluster.tsp.api.admin.resrce.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ResrceHistParam implements Serializable {

    private static final long serialVersionUID = 8326680213387964526L;

    @ApiModelProperty(value = "이력ID")
    private String histId;
    @ApiModelProperty(value = "신청ID")
    private String reqstId;
    @ApiModelProperty(value = "처리자ID")
    private String opetrId;
    @ApiModelProperty(value = "처리구분")
    private String processKnd;
    @ApiModelProperty(value = "처리사유")
    private String processResn;
    @ApiModelProperty(value = "처리일시")
    private Date creatDt;
    @ApiModelProperty(value = "회원ID")
    private String mberId;
    @ApiModelProperty(value = "회원명(사업자명)")
    private String mberNm;

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


}
