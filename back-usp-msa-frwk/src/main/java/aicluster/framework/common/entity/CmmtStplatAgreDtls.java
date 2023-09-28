package aicluster.framework.common.entity;

import java.io.Serializable;
import java.util.Date;

import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmmtStplatAgreDtls implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 4399297890453698312L;

	private String  termsType;			/* 약관유형(G:TERMS_TYPE) */
	private String  beginDay;			/* 시작일(시행일) */
	private boolean required;			/* 필수여부(true:필수, false:선택) */
	private String  memberId;			/* 회원ID */
	private Date    consentDt;			/* 약관동의일시 */
	private String  possessExpiredDay;	/* 보유만료일(약관동의만료일) */
	private boolean consentYn;			/* 동의여부(true:동의, false:미동의) */

	public String getFmtBeginDay() {
		if (string.isBlank(this.beginDay) || !date.isValidDate(this.beginDay, "yyyyMMdd")) {
			return null;
		}
		return date.format(string.toDate(this.beginDay), "yyyy-MM-dd");
	}

	public String getFmtConsentDt() {
		if (this.consentDt == null) {
			return null;
		}
		return date.format(this.consentDt, "");
	}
	
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
