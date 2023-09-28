package aicluster.tsp.api.admin.eqpmn.estmt.param;

import aicluster.tsp.common.dto.ApplcntDto;
import bnet.library.util.CoreUtils;
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
@ApiModel(description = "견적요청 상세 > 신청정보 신청정보")
public class EstmtOzReportParam implements Serializable {
	private static final long serialVersionUID = -4257184165675599473L;
	@JsonIgnore
	@ApiModelProperty(value = "담당자ID")
	private String mberId;
	@ApiModelProperty(value = "담당자 로그인 ID")
	private String loginId;
	@ApiModelProperty(value = "담당자명")
	private String mberNm;
	@JsonIgnore
	@ApiModelProperty(value = "담당자 전화번호")
	private String encptTelno;
	@JsonIgnore
	@ApiModelProperty(value = "담당자 Email")
	private String encptEmail;
	@ApiModelProperty(value = "담당자 직급")
	private String clsfNm;
	@ApiModelProperty(value = "견적서 발급일자.")
	private Date creatDt;

	ApplcntDto applcnt;

	public String getTelno() {
		if (CoreUtils.string.isBlank(this.encptTelno)) {
			return null;
		}
		return CoreUtils.aes256.decrypt(this.encptTelno, this.mberId);
	}
	public String getEmail() {
		if (CoreUtils.string.isBlank(this.encptEmail)) {
			return null;
		}
		return CoreUtils.aes256.decrypt(this.encptEmail, this.mberId);
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


}