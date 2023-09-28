package aicluster.tsp.api.admin.eqpmn;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(description = "장비목록 검색 리스트")
public class EqpmnAllListParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1990446096838213082L;
	@ApiModelProperty(value = "장비상태(G:EQPMN_ST)")
	private String eqpmnSttus;
	@ApiModelProperty(value = "자산번호")
	private String assetsNo;
	@ApiModelProperty(value = "장비명(국문)")
	private String eqpmnNmKorean;
	@ApiModelProperty(value = "모델명")
	private String modelNm;
	@ApiModelProperty(value = "반출여부")
	private Boolean tkoutAt;
	@ApiModelProperty(value = "불용여부")
	private Boolean disuseAt;
	@ApiModelProperty(value = "시작일")
	private Date beginDay;
	@ApiModelProperty(value = "종료일")
	private Date endDay;
	public Date getBeginDay() {
		if (this.beginDay != null) {
			return new Date(this.beginDay.getTime());
		}
		return null;
	}
	public void setBeginDay(Date beginDay) {
		this.beginDay = null;
		if (beginDay != null) {
			this.beginDay = new Date(beginDay.getTime());
		}
	}
	public Date getEndDay() {
		if (this.endDay != null) {
			return new Date(this.endDay.getTime());
		}
		return null;
	}
	public void setEndDay(Date endDay) {
		this.endDay = null;
		if (endDay != null) {
			this.endDay = new Date(endDay.getTime());
		}
	}
}