package aicluster.mvn.common.dto;

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
public class MvnFcRsvtListItemDto implements Serializable {

	private static final long serialVersionUID = 408233926418426958L;

	private String  reserveId		;       /** 예약ID                            */
	private String  mvnFcId			;       /** 입주시설ID                        */
	private String  reserveSt		;       /** 예약상태(G:RESERVE_ST)            */
	private String  reserveStNm		;       /** 예약상태명 : CMMT_CODE.CODE_NM */
	private String  mvnFcDtype		;       /** 입주시설세부유형(G:MVN_FC_DTYPE)  */
	private String  mvnFcDtypeNm	;	       /** 입주시설세부유형명 : CMMT_CODE.CODE_NM */
	private String  mvnFcNm			;       /** 입주시설명                        */
	private String  reserveType		;       /** 예약유형(G:RESERVE_TYPE)          */
	private String  reserveTypeNm	;	       /** 예약유형명 : CMMT_CODE.CODE_NM */
	private String  rsvtDay			;       /** 예약일                            */
	private String  rsvtBgngTm		;       /** 예약시작시간                      */
	private String  rsvtEndTm		;       /** 예약종료시간                      */
	//private String  rsvtDtPeriod	;	       /** 예약일시기간                      */
	private Integer rsvtNope		;       /** 예약인원수                        */
	private String  rsvctmId		;       /** 예약자ID                          */
	private String  rsvctmNm		;       /** 예약자명 : CMMT_MEMBER.MEMBER_NM */
	private Boolean  mvnYn			;       /** 입주여부(true:입주, false:미입주) */
	private Date    rsvtReqDt		;       /** 예약요청일시                      */

	private long rn;

	public String getRsvtDtPeriod() {
		if (string.isBlank(rsvtDay)) {
			return "";
		}
		Date beginDt = string.toDate(rsvtDay + rsvtBgngTm);
		Date endDt = string.toDate(rsvtDay + rsvtEndTm);
		String beginTm = date.format(beginDt, "yyyy-MM-dd HH:mm");
		String endTm = date.format(endDt, "HH:mm");
		long diff = date.getDiffMinutes(beginDt, endDt);
		long dh = diff / 60;
		long dm = diff % 60;

		String val = beginTm + " ~ " + endTm;
		if (dh > 0) {
			val += String.format("(%d시간 %d분)", dh, dm);
		}
		else {
			val += String.format("(%d분)", dm);
		}

		return val;
	}
}
