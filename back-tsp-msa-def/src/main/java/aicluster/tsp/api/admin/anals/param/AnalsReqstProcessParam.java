package aicluster.tsp.api.admin.anals.param;

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
@ApiModel(description = "분석도구 신청 처리")
public class AnalsReqstProcessParam implements Serializable {
	/**
	 * 
	 */
	@ApiModelProperty(value = "신청ID")
	private String reqstId;
	@ApiModelProperty(value = "신청 상태")
	private String useSttus;
	@ApiModelProperty(value = "사유")
	private String rsndqf;
	@ApiModelProperty(value = "수정자", hidden = true)
	private String updusrId;
	@ApiModelProperty(value = "수정일시", hidden = true)
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