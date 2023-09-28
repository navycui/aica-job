package aicluster.common.api.survey.dto;

import java.io.Serializable;
import java.util.Date;

import bnet.library.util.CoreUtils.date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurveyListParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 6525027101743535246L;

	private String systemId;
	private String surveySt;
	private Date beginDay;
	private Date endDay;
	private String surveyNm;

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
