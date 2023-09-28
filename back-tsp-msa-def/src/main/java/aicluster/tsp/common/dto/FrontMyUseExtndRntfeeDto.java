package aicluster.tsp.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "사용료부과내역 조회")
public class FrontMyUseExtndRntfeeDto implements Serializable {
	private static final long serialVersionUID = 3526350324116531772L;

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class MyRntfee {
		@ApiModelProperty(value = "구분")
		private String div;
		@ApiModelProperty(value = "사용시작시간")
		private Date useBeginDt;
		@ApiModelProperty(value = "사용종료시간")
		private Date useEndDt;
		@ApiModelProperty(value = "사용료")
		private Integer rntfee;
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
	List<MyRntfee> myRntfee;

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class MyAddRntfee {
		@ApiModelProperty(value = "구분")
		private String div;
		@ApiModelProperty(value = "추가금")
		private Integer aditRntfee;
		@ApiModelProperty(value = "청구사유")
		private String rqestResn;
	}
	List<MyAddRntfee> myAddRntfee;
}