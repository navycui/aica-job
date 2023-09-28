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
@ApiModel(description = "사용신청 반출심의결과 처리")
public class UseReqstTkoutProcessParam implements Serializable {
	/**
	 * 
	 */
	@ApiModelProperty(value = "장비견적신청ID")
	private String reqstId;
	@ApiModelProperty(value = "반출심의 결과")
	private String tkoutDlbrtResult;
	@ApiModelProperty(value = "반출심의 내용")
	private String tkoutDlbrtCn;

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