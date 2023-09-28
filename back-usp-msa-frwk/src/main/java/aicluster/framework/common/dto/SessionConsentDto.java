package aicluster.framework.common.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SessionConsentDto implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 7369539219975940754L;

	private String  termsType;			/* 약관유형(G:TERMS_TYPE) */
	private String  beginDay;			/* 시작일(시행일) */
	private boolean required;			/* 필수여부(true:필수, false:선택) */
	private Date    consentDt;			/* 약관동의일시 */
	private boolean consentYn;			/* 동의여부(true:동의, false:미동의) */
	
	public Date getConsentDt() {
		if (this.consentDt != null) {
			return new Date(this.consentDt.getTime());
		}
		return null;
	}
	
	public void setConsentDt(Date consentDt) {
		this.consentDt = null;
		if (consentDt != null) {
			this.consentDt = new Date(consentDt.getTime());
		}
	}
}
