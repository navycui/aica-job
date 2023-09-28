package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UsptTaskTaxitmWct implements Serializable {

	/**
	 * 과제세목별사업비
	 */
	private static final long serialVersionUID = -4640517135259401957L;
	/** 과제세목별사업비ID */
	String  taskTaxitmWctId;
	/** 사업계획서ID */
	String  bsnsPlanDocId;
	/**사업년도*/
	String bsnsYear;
	/** 사업비세목ID */
	String  wctTaxitmId;
	/** 사업비세목명 */
	String wctTaxitmNm;
	/** 산출근거내용 */
	String  computBasisCn;
	/** 지원예산 */
	Long sportBudget;
	/** 부담금현금 */
	Long alotmCash;
	/** 부담금현물 */
	Long alotmActhng;
	/** 합계 (지원예산+현금+현물) */
	Long alotmSumTot;
	/**사업비비목ID*/
	String	wctIoeId;
	/**사업비비목명*/
	String	wctIoeNm;

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
