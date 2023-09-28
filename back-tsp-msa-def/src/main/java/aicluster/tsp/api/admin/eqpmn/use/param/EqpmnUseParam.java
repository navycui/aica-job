package aicluster.tsp.api.admin.eqpmn.use.param;

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
@ApiModel(description = "장비 대여 >  장비신청 관리 검색 조건 Param")
public class EqpmnUseParam implements Serializable {

	private static final long serialVersionUID = 3525446663634590944L;

	@ApiModelProperty(value = "장비대여신청상태(G:EQPMN_RENTAL_ST)")
	private String useSttus;
	@ApiModelProperty(value = "반출 여부")
	private Boolean tkoutAt;
	@ApiModelProperty(value = "반입 여부")
	private Boolean tkinAt;
	@ApiModelProperty(value = "지불방법(G:PAYMENT_METHOD)")
	private String pymntMth;

	@ApiModelProperty(value = "사용시작일")
	private Date useBeginDt;
	@ApiModelProperty(value = "사용종료일")
	private Date useEndDt;

	@ApiModelProperty(value = "장비명")
	private String eqpmnNmKorean;
	@ApiModelProperty(value = "자산번호")
	private String assetsNo;
	@ApiModelProperty(value = "이름/사업자명")
	private String entrprsNm;
	@ApiModelProperty(value = "접수번호")
	private String rceptNo;
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
