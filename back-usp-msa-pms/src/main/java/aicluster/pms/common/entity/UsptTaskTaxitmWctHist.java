package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UsptTaskTaxitmWctHist implements Serializable {
	/**
	 *과제세목별사업비변경이력
	 */
	private static final long serialVersionUID = -4544450133324285361L;

	/** 과제신청사업비변경이력ID */
	String  taskTaxitmWctHistId;
	/** 협약변경요청ID */
	String  cnvnChangeReqId;
	/** 승인과제세목별사업비변경이력ID */
	String  confmTaskTaxitmWctHistId;
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

	/** 사업년도 */
	String  bsnsYear;

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