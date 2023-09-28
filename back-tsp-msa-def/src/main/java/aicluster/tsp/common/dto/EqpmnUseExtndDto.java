package aicluster.tsp.common.dto;

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
@ApiModel(description = "장비사용 기간연장관리 DTO")
public class EqpmnUseExtndDto implements Serializable {

	private static final long serialVersionUID = 8126902709889699942L;
	@ApiModelProperty(value = "연장신청ID")
	private String etReqstId;
	@ApiModelProperty(value = "신청상태")
	private String reqstSttus;
	@ApiModelProperty(value = "사용 시작일")
	private Date useBeginDt;
	@ApiModelProperty(value = "사용 종료일")
	private Date useEndDt;
	@ApiModelProperty(value = "할인ID")
	private String dscntId;
	@ApiModelProperty(value = "할인율")
	private Integer dscntRate;
	@ApiModelProperty(value = "사용시간")
	private Integer usgtm;
	@ApiModelProperty(value = "1시간 사용료")
	private Integer rntfeeHour;
	@ApiModelProperty(value = "사용료")
	private Integer rntfee;
	@ApiModelProperty(value = "할인금액")
	private Integer dscntAmount;
	@ApiModelProperty(value = "지불방법")
	private String pymntMth;

	@ApiModelProperty(value = "장비Id",hidden = true)
	private String eqpmnId;
	@ApiModelProperty(value = "반출여부",hidden = true)
	private Boolean tkoutAt;

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