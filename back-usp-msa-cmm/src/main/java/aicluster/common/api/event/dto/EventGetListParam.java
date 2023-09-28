package aicluster.common.api.event.dto;

import java.io.Serializable;
import java.util.Date;

import bnet.library.util.CoreUtils.date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventGetListParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -6684758203058250464L;

	private Boolean posting;
	private Date beginDay;
	private Date endDay;
	private String eventNm;

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
