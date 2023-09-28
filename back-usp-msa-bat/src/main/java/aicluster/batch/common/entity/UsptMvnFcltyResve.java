package aicluster.batch.common.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsptMvnFcltyResve implements Serializable {

	private static final long serialVersionUID = -6809497382580927691L;

	private String  reserveId     ;                 /** 예약ID */
	private String  mvnFcId       ;                 /** 입주시설ID */
	private String  rsvctmId      ;                 /** 예약자ID(회원ID) */
	private String  rsvtDay       ;                 /** 예약일 */
	private String  rsvtBgngTm    ;                 /** 예약시작시각 : HH24MISS 형식 */
	private String  rsvtEndTm     ;                 /** 예약종료시각 : HH24MISS 형식 */
	private Date    rsvtReqDt     ;                 /** 예약요청일시 */
	private String  reserveSt     ;                 /** 예약상태(G:RESERVE_ST) */
	private Date    reserveStDt   ;                 /** 예약상태변경일시 */
	private String  updaterId     ;                 /** 수정자ID(CMMT_MEMBER.MEMBER_ID) */
	private Date    updatedDt     ;                 /** 수정일시 */
	
	public Date getRsvtReqDt() {
		if (this.rsvtReqDt != null) {
			return new Date(this.rsvtReqDt.getTime());
		}
		return null;
	}
	
	public void setRsvtReqDt(Date rsvtReqDt) {
		this.rsvtReqDt = null;
		if (rsvtReqDt != null) {
			this.rsvtReqDt = new Date(rsvtReqDt.getTime());
		}
	}
	
	public Date getReserveStDt() {
		if (this.reserveStDt != null) {
			return new Date(this.reserveStDt.getTime());
		}
		return null;
	}
	
	public void setReserveStDt(Date reserveStDt) {
		this.reserveStDt = null;
		if (reserveStDt != null) {
			this.reserveStDt = new Date(reserveStDt.getTime());
		}
	}
	
	public Date getUpdatedDt() {
		if (this.updatedDt != null) {
			return new Date(this.updatedDt.getTime());
		}
		return null;
	}
	
	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = null;
		if (updatedDt != null) {
			this.updatedDt = new Date(updatedDt.getTime());
		}
	}
}
