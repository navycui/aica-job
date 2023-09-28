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
@ApiModel(description = "결과보고서 처리이력")

public class TsptEqpmnReprtHist implements Serializable {

    private static final long serialVersionUID = 7368091860827142248L;

    @ApiModelProperty(value = "이력ID")
    private String histId;
    @ApiModelProperty(value = "보고서ID")
    private String reprtId;
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
