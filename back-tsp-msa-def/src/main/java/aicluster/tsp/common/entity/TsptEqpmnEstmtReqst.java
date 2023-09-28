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
@ApiModel(description = "장비견적신청")
public class TsptEqpmnEstmtReqst implements Serializable {
	private static final long serialVersionUID = -6236482313386595278L;

	@ApiModelProperty(value = "견적ID")
	private String estmtId;
	@ApiModelProperty(value = "신청자ID")
	private String applcntId;
	@ApiModelProperty(value = "장비ID")
	private String eqpmnId;
	@ApiModelProperty(value = "지불방법")
	private String pymntMth;
	@ApiModelProperty(value = "활용목적")
	private String useprps;
	@ApiModelProperty(value = "반출여부")
	private boolean tkoutAt;
	@ApiModelProperty(value = "반출지주소")
	private String tkoutAdres;
	@ApiModelProperty(value = "반출사유")
	private String tkoutResn;
	@ApiModelProperty(value = "첨부파일그룹ID")
	private String atchmnflGroupId;
	@ApiModelProperty(value = "사용시작시간")
	private Date useBeginDt;
	@ApiModelProperty(value = "사용종료시간")
	private Date useEndDt;
	@ApiModelProperty(value = "반출심의결과")
	private String tkoutDlbrtResult;
	@ApiModelProperty(value = "반출심의내용")
	private String tkoutDlbrtCn;
	@ApiModelProperty(value = "신청상태")
	private String reqstSttus;
	@ApiModelProperty(value = "보완사유")
	private String rsndqf;
	@ApiModelProperty(value = "사용료")
	private Integer rntfee;
	@ApiModelProperty(value = "추가금")
	private Integer aditRntfee;
	@ApiModelProperty(value = "청구사유")
	private String rqestResn;
	@ApiModelProperty(value = "사용시간")
	private Integer usgtm;
	@ApiModelProperty(value = "할인ID")
	private String dscntId;
	@ApiModelProperty(value = "접수번호")
	private String rceptNo;
	@ApiModelProperty(value = "생성자ID")
	private String creatrId;
	@ApiModelProperty(value = "생성일시")
	private Date creatDt;
	@ApiModelProperty(value = "수정자ID")
	private String updusrId;
	@ApiModelProperty(value = "수정일시")
	private Date updtDt;

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


}
