package aicluster.tsp.common.entity;

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
@ApiModel(description = "장비 처리이력")

public class TsptEqpmnProcessHist implements Serializable {

    private static final long serialVersionUID = 8271146836846182565L;

    @ApiModelProperty(value = "처리ID")
    private String histId;
    @ApiModelProperty(value = "장비ID")
    private String eqpmnId;
    @ApiModelProperty(value = "처리자ID")
    private String opetrId;
    @ApiModelProperty(value = "처리구분")
    private String processKnd;
    @ApiModelProperty(value = "처리사유")
    private String processResn;
    @ApiModelProperty(value = "생성일시")
    private Date creatDt;
    @ApiModelProperty(value = "회원ID")
    private String mberId;
    @ApiModelProperty(value = "회원명")
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
