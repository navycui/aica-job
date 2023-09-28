package aicluster.common.api.terms.dto;

import java.io.Serializable;
import java.util.Date;

import bnet.library.util.CoreUtils.date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TermsConsentParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 5432185880308190532L;

	private String  termsType;
	private Date    beginDay;
	private boolean required;
	private Boolean consentYn;

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
}
