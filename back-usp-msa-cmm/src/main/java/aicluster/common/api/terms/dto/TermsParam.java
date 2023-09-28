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
public class TermsParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -4792590488389313772L;

	private String termsType;			/* 약관유형 */
	private Date   beginDay;			/* 시작일(=시행일) */
	private String requiredTermsCn;		/* 필수 약관내용 */
	private String optionedTermsCn;		/* 선택 약관내용 */
	private String possessTermCd;		/* 보유 기간 */

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
