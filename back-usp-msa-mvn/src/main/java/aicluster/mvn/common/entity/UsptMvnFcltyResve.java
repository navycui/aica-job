package aicluster.mvn.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class UsptMvnFcltyResve implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 3705263971410003869L;

	private String  reserveId     ;                 /** 예약ID */
	private String  mvnFcId       ;                 /** 입주시설ID */
	private String  rsvctmId      ;                 /** 예약자ID(회원ID) */
	private String  rsvtDay       ;                 /** 예약일 */
	private String  rsvtBgngTm    ;                 /** 예약시작시각 : HH24MISS 형식 */
	private String  rsvtEndTm     ;                 /** 예약종료시각 : HH24MISS 형식 */
	private Integer rsvtNope      ;                 /** 예약인원수 */
	private String  utztnPurpose    ;                 /** 이용목적 */
	private Boolean mvnYn         ;                 /** 입주여부 */
	private Date    rsvtReqDt     ;                 /** 예약요청일시 */
	private String  reserveSt     ;                 /** 예약상태(G:RESERVE_ST) */
	private Date    reserveStDt   ;                 /** 예약상태변경일시 */
	private String  rejectReasonCn;                 /** 반려사유내용 */
	@JsonIgnore
	private String  creatorId     ;                 /** 생성자ID(CMMT_MEMBER.MEMBER_ID) */
	@JsonIgnore
	private Date    createdDt     ;                 /** 생성일시 */
	@JsonIgnore
	private String  updaterId     ;                 /** 수정자ID(CMMT_MEMBER.MEMBER_ID) */
	@JsonIgnore
	private Date    updatedDt     ;                 /** 수정일시 */

	/*
	 * HELPER
	 */
	private String reserveStNm;

	public String getFmtRsvtReqDt() {
		if (this.rsvtReqDt == null) {
			return null;
		}
		return date.format(this.rsvtReqDt, "yyyy-MM-dd HH:mm:ss");
	}

	public String getFmtReserveStDt() {
		if (this.reserveStDt == null) {
			return null;
		}
		return date.format(this.reserveStDt, "yyyy-MM-dd HH:mm:ss");
	}

	public String getFmtRsvtDay() {
		if (string.isBlank(this.rsvtDay) || !date.isValidDate(this.rsvtDay, "yyyyMMdd")) {
			return null;
		}
		return date.format(string.toDate(this.rsvtDay), "yyyy-MM-dd");
	}

	public String getFmtRsvtTm() {
		String beginTm = "";
		String endTm = "";
		if (string.length(rsvtBgngTm) == 4) {
			beginTm = string.substring(rsvtBgngTm, 0, 2) + ":" + string.substring(rsvtBgngTm, 2, 4);
		}
		if (string.length(rsvtEndTm) == 4) {
			endTm = string.substring(rsvtEndTm, 0, 2) + ":" + string.substring(rsvtEndTm, 2, 4);
		}

		return beginTm + " ~ " + endTm;
	}
}
