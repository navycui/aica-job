package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UsptTaskPrtcmpnyInfoHist implements Serializable {
	/**
	 * 과제참여기업정보변경이력
	 */
	private static final long serialVersionUID = 2554444483025074840L;
	/** 과제참여기업정보변경이력ID */
	String  taskPrtcmpnyInfoHistId;
	 /** 협약변경요청ID */
	String  cnvnChangeReqId;
	/** 승인과제참여기업정보변경이력ID */
	String  confmTaskPrtcmpnyInfoHistId;
	/** 참여기업수 */
	Long    partcptnCompanyCnt;
	/** 중소기업수 */
	Long    smlpzCnt;
	/** 중견기업수 */
	Long    mspzCnt;
	/** 기타수 */
	Long    etcCnt;
	 /*변경일자*/
	String changeDe;
	 /*변경전후*/
	String changeBeAf;

	/** 생성자ID */
	@JsonIgnore
	private String creatorId;
	/** 생성일시 */
	@JsonIgnore
	private Date createdDt;

	public Date getCreatedDt() {
		if (this.createdDt != null) {
			return new Date(this.createdDt.getTime());
         }
		return null;
	}
	public void setCreatedDt(Date createdDt) {
		this.createdDt = null;
		if (createdDt != null) {
			this.createdDt = new Date(createdDt.getTime());
		}
	}

}
