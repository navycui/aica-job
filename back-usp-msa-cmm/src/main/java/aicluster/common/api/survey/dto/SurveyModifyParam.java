package aicluster.common.api.survey.dto;

import java.io.Serializable;
import java.util.Date;

import bnet.library.util.CoreUtils.date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyModifyParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -2406148082487279847L;
	private String surveyId;
	private String surveyNm;
	private String systemId;
	private Date beginDay;
	private Date endDay;
	private String remark;
	private Boolean enabled;
	private Boolean duplicated;

	public String getBeginDay() {
		if (this.beginDay == null) {
			return null;
		}

		return date.format(this.beginDay, "yyyyMMdd");
	}
	
	public void setBeginDay(Date beginDay) {
		this.beginDay = null;
		if (beginDay != null) {
			this.beginDay = new Date(beginDay.getTime());
		}
	}

	public String getEndDay() {
		if (this.endDay == null) {
			return null;
		}
		return date.format(this.endDay, "yyyyMMdd");
	}
	
	public void setEndDay(Date endDay) {
		this.endDay = null;
		if (endDay != null) {
			this.endDay = new Date(endDay.getTime());
		}
	}
}
