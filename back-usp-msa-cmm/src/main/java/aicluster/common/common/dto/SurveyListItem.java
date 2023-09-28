package aicluster.common.common.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SurveyListItem implements Serializable {

	private static final long serialVersionUID = -1443795531546736348L;

	private String surveyId;
	private String surveyNm;
	private String systemId;
	private String systemNm;
	private String beginDay;
	private String endDay;
	private Boolean enabled;
	private Boolean duplicated;
	@JsonIgnore
	private String creatorId;
	private String creatorNm;
	private Date createdDt;
	private String surveyStNm;
	private Boolean partcptnAt;	/** 설문참여여부 */
	private Long rn;

	public String getFmtBeginDay() {
		if (string.isBlank(this.beginDay)) {
			return null;
		}

		return date.format(string.toDate(this.beginDay), "yyyy-MM-dd");
	}

	public String getFmtEndDay() {
		if (string.isBlank(this.endDay)) {
			return null;
		}

		return date.format(string.toDate(this.endDay), "yyyy-MM-dd");
	}

	public String getFmtCreatedDt() {
		if (this.createdDt == null) {
			return null;
		}
		return date.format(this.createdDt, "yyyy-MM-dd HH:mm:ss");
	}
	public String getFmtCreatedDay() {
		if (this.createdDt == null) {
			return null;
		}
		return date.format(this.createdDt, "yyyy-MM-dd");
	}
	
	public Date getCreatedDt() {
		if (this.createdDt != null) {
			return new Date(this.createdDt.getTime());
		}
		return null;
	}
	
	public void setCreatedDt(Date createdDt) {
		this.createdDt = null;
		if (createdDt != null) {
			this.createdDt = new Date(createdDt.getTime());
		}
	}
}
