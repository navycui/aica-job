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
@ApiModel(description = "장비설정")

public class TsptEqpmnDetail implements Serializable{

    private static final long serialVersionUID = -1945865518574002766L;

    @ApiModelProperty(value = "장비ID")
    private String eqpmnId;
    @ApiModelProperty(value = "시간당 사용료")
    private Integer rntfeeHour;
    @ApiModelProperty(value = "1일 가용시작시간")
    private Integer usefulBeginHour;
    @ApiModelProperty(value = "1일 가용종료시간")
    private Integer usefulEndHour;
    @ApiModelProperty(value = "사용율 저조설정")
    private boolean useRateInctvSetupAt;
    @ApiModelProperty(value = "법정공휴일 휴일포함")
    private boolean hldyInclsAt;
    @ApiModelProperty(value = "반출시 휴일포함")
    private boolean tkoutHldyInclsAt;
    @ApiModelProperty(value = "미반출시 휴일포함")
    private boolean nttkoutHldyInclsAt;
    @ApiModelProperty(value = "교정주기(일)")
    private Integer crrcCycle;
    @ApiModelProperty(value = "교정대상여부")
    private boolean crrcTrgetAt;
    @ApiModelProperty(value = "점검대상여부")
    private boolean chckTrgetAt;
    @ApiModelProperty("불용여부")
    private boolean disuseAt;
    @ApiModelProperty("교정ID")
    private String crrcId;
    @ApiModelProperty("수리ID")
    private String repairId;
    @ApiModelProperty("점검ID")
    private String chckId;
    @ApiModelProperty("생성자ID")
    private String creatrId;
    @ApiModelProperty("생성일시")
    private Date creatDt;
    @ApiModelProperty("수정자ID")
    private String updusrId;
    @ApiModelProperty("수정일시")
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
