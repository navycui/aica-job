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
@ApiModel(description = "장비연장신청")

public class TsptEqpmnExtn implements Serializable {

    private static final long serialVersionUID = 1677018731794421457L;

    @ApiModelProperty(value = "연장신청ID")
    private String etReqstId;
    @ApiModelProperty(value = "사용신청ID")
    private String reqstId;
    @ApiModelProperty(value = "신청상태")
    private String reqstSttus;
    @ApiModelProperty(value = "사용시작일")
    private Date useBeginDt;
    @ApiModelProperty(value = "사용종료일")
    private Date useEndDt;
    @ApiModelProperty(value = "사용료")
    private Integer rntfee;
    @ApiModelProperty(value = "할인ID")
    private String dscntId;
    @ApiModelProperty(value = "사용시간")
    private Integer usgtm;
    @ApiModelProperty(value = "사유(보완, 반려)")
    private String rsndqf;
    @ApiModelProperty(value = "생성자ID")
    private String creatrId;
    @ApiModelProperty(value = "생성일시")
    private Date creatDt;
    @ApiModelProperty(value = "수정자ID")
    private String updusrId;
    @ApiModelProperty(value = "수정일시")
    private Date updtDt;
    @ApiModelProperty(value = "입금안내문")
    private String rcpmny_gdcc;
    @ApiModelProperty(value = "입금안내문일시")
    private Date rcpmny_gdcc_dt;
    @ApiModelProperty(value = "미납사유")
    private String npy_resn;
    @ApiModelProperty(value = "예상사용료")
    private Integer expectRntfee;
    @ApiModelProperty(value = "할인금액")
    private Integer dscntAmount;
    @ApiModelProperty(value = "예상사용시간")
    private Integer expectUsgtm;

    public Date getUseBeginDt() {
		if (this.useBeginDt != null) {
			return new Date(this.useBeginDt.getTime());
		}
		return null;
	}
    public void setUseBeginDt(Date useBeginDt) {
		this.useBeginDt = null;
		if (useBeginDt != null) {
			this.useBeginDt = new Date(useBeginDt.getTime());
		}
	}
    public Date getUseEndDt() {
    if (this.useEndDt != null) {
      return new Date(this.useEndDt.getTime());
    }
    return null;
  }
    public void setUseEndDt(Date useEndDt) {
        this.useEndDt = null;
        if (useEndDt != null) {
            this.useEndDt = new Date(useEndDt.getTime());
        }
    }
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
    public Date getRcpmny_gdcc_dt() {
        if (this.rcpmny_gdcc_dt != null) {
            return new Date(this.rcpmny_gdcc_dt.getTime());
        }
        return null;
    }
    public void setRcpmny_gdcc_dt(Date rcpmny_gdcc_dt) {
        this.rcpmny_gdcc_dt = null;
        if (rcpmny_gdcc_dt != null) {
            this.rcpmny_gdcc_dt = new Date(rcpmny_gdcc_dt.getTime());
        }
    }

}
