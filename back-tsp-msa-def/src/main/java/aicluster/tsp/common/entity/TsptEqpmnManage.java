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
@ApiModel(description = "장비관리")

public class TsptEqpmnManage implements Serializable {

    private static final long serialVersionUID = 1586758422168357738L;

    @ApiModelProperty(value = "관리ID")
    private String manageId;
    @ApiModelProperty(value = "장비ID")
    private String eqpmnId;
    @ApiModelProperty(value = "관리시작일")
    private Date manageBeginDt;
    @ApiModelProperty(value = "관리종료일")
    private Date manageEndDt;
    @ApiModelProperty(value = "관리구분")
    private String manageDiv;
    @ApiModelProperty(value = "관리사유")
    private String manageResn;
    @ApiModelProperty(value = "관리결과")
    private String manageResult;
    @ApiModelProperty(value = "교정기관")
    private String crrcInstt;
    @ApiModelProperty(value = "처리자ID")
    private String opetrId;
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

    public Date getManageBeginDt() {
		if (this.manageBeginDt != null) {
			return new Date(this.manageBeginDt.getTime());
		}
		return null;
	}
    public void setManageBeginDt(Date manageBeginDt) {
		this.manageBeginDt = null;
		if (manageBeginDt != null) {
			this.manageBeginDt = new Date(manageBeginDt.getTime());
		}
	}
    public Date getManageEndDt() {
        if (this.manageEndDt != null) {
            return new Date(this.manageEndDt.getTime());
        }
        return null;
    }
    public void setManageEndDt(Date manageEndDt) {
        this.manageEndDt = null;
        if (manageEndDt != null) {
            this.manageEndDt = new Date(manageEndDt.getTime());
        }
    }

}
