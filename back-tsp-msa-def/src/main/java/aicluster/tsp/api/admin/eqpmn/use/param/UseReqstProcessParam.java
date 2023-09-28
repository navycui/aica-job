package aicluster.tsp.api.admin.eqpmn.use.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@ApiModel(description = "사용신청 처리")
public class UseReqstProcessParam implements Serializable {
	/**
	 * 
	 */
	@ApiModelProperty(value = "장비사용신청ID")
	private String reqstId;
	@ApiModelProperty(value = "신청 상태")
	private String reqstSttus;
	@ApiModelProperty(value = "사유")
	private String rsndqf;
	@ApiModelProperty(value = "접수번호")
	private String rceptNo;

	@ApiModelProperty(value = "수정자")
	private String updusrId;
	@ApiModelProperty(value = "수정일시")
	private Date updtDt;
	@JsonIgnore
	@ApiModelProperty(value = "신청자ID")
	private String applcntId;
	@JsonIgnore
	@ApiModelProperty(value = "장비ID")
	private String eqpmnId;
	@JsonIgnore
	@ApiModelProperty(value = "장비ID")
	private String estmtId;
	@ApiModelProperty(value = "사용시작일")
	private Date useBeginDt;
	@ApiModelProperty(value = "사용종료일")
	private Date useEndDt;

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

}