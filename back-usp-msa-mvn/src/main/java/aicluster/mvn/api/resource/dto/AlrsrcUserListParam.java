package aicluster.mvn.api.resource.dto;

import java.io.Serializable;
import java.util.Date;

import bnet.library.util.CoreUtils.date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlrsrcUserListParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1662291595395306721L;

	private String cmpnyId    ;        /* 업체ID(CMMV_MEMBER.MEMBER_ID) */
	private Date alrsrcBgngDay;        /* 자원할당시작일                */
	private Date alrsrcEndDay ;        /* 자원할당종료일                */
	private String alrsrcSt   ;        /* 자원할당상태(G:ALRSRC_ST)     */

	public String getAlrsrcBgngDay() {
		if (this.alrsrcBgngDay == null) {
			return null;
		}
		return date.format(this.alrsrcBgngDay, "yyyyMMdd");
	}

	public String getAlrsrcEndDay() {
		if (this.alrsrcEndDay == null) {
			return null;
		}
		return date.format(this.alrsrcEndDay, "yyyyMMdd");
	}
}
