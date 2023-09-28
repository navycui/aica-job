package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UsptTaskReqstWctHist implements Serializable {

	/**
	 * 과제신청사업비변경이력
	 */
	private static final long serialVersionUID = -7058033537759184992L;

	/** 과제신청사업비변경이력ID */
	String  taskReqstWctHistId;
	/** 협약변경요청ID */
	String  cnvnChangeReqId;
	/** 승인과제신청사업비변경이력ID */
	String  confmTaskReqstWctHistId;
	/** 사업년도 */
	String  bsnsYear;
	 /** 총사업비*/
	Long totalWct;
	/** 지원예산 */
	Long sportBudget;
	 /** 부담금현금 */
	Long alotmCash;
	/** 부담금현물 */
	Long alotmActhng;
	/** 부담금소계 (현금+현물) */
	Long alotmSum;
	/** 부담금합계 (지원예산+현금+현물) */
	Long alotmSumTot;
	 /*변경일자*/
	String changeDe;
	 /*변경전후*/
	String changeBeAf;

	@JsonIgnore
	private String creatorId;		/** 생성자 ID */
	@JsonIgnore
	private Date createdDt;			/** 생성일시 */

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
