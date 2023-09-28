package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UsptTaskReqstWct implements Serializable {

	/**
	 * 과제신청사업비
	 */
	private static final long serialVersionUID = 8665241039339745996L;
	/** 과제신청사업비ID */
	String  taskReqstWctId;
	/** 사업계획서ID */
	String  bsnsPlanDocId;
	/** 사업년도 */
	String  bsnsYear;
	 /** 총사업비 */
	Long totalWct;
	/** 지원예산 */
	Long sportBudget;
	 /** 부담금현금 */
	Long alotmCash;
	/** 부담금현물 */
	Long alotmActhng;
	/** 부담금소계 (현금+현물) */
	Long alotmSum;
	/** 합계 (지원예산+현금+현물) */
	Long alotmSumTot;

	/** 순번 */
	private int rn;

	/** 페이징 처리 */
	@JsonIgnore
	private Long beginRowNum;
	@JsonIgnore
	private Long itemsPerPage;

	/** I:등록, U:수정, D:삭제 */
	private String flag;

	@JsonIgnore
	private String creatorId;		/** 생성자 ID */
	@JsonIgnore
	private Date createdDt;			/** 생성일시 */
	@JsonIgnore
	String  updaterId;              /** 수정자ID(CMMT_MEMBER.MEMBER_ID) */
	@JsonIgnore
	private Date updatedDt;         /** 수정일시 */

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
