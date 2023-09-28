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
@ApiModel(description = "장비사용 기간연장")
public class UseExtndParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4628888790065302307L;
	@ApiModelProperty(value = "장비견적신청ID")
	private String ReqstId;
	@ApiModelProperty(value = "사용시작 시간")
	private Date useBeginDt;
	@ApiModelProperty(value = "사용종료 시간")
	private Date useEndDt;
	@ApiModelProperty(value = "사용료")
	private Integer rntfee;
	@ApiModelProperty(value = "사용시간")
	private Integer usgtm;
	@ApiModelProperty(value = "예상사용료")
	private Integer expectRntfee;
	@ApiModelProperty(value = "예상사용시간")
	private Integer expectUsgtm;
	@ApiModelProperty(value = "할인금액")
	private Integer dscntAmount;
	@ApiModelProperty(value = "사유")
	private String rsndqf;
	@ApiModelProperty(value = "수정자")
	private String updusrId;
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