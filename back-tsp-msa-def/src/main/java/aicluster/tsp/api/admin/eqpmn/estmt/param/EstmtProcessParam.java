package aicluster.tsp.api.admin.eqpmn.estmt.param;

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
@ApiModel(description = "견적요청 처리")
public class EstmtProcessParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8955262519402651938L;
	@ApiModelProperty(value = "장비견적신청ID")
	private String estmtId;
	@ApiModelProperty(value = "신청 상태")
	private String reqstSttus;
	@ApiModelProperty(value = "사유")
	private String rsndqf;

	@ApiModelProperty(value = "수정자")
	private String updusrId;
	@ApiModelProperty(value = "수정일시")
	private Date updtDt;

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