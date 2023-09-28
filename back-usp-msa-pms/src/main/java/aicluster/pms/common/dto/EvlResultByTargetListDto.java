package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class EvlResultByTargetListDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -2201538765596118165L;

	private int rn;							/** 순번 */
	String  flag; 							/** 변경 플래그(I,U,D) */
	private boolean isCheck;				/** 선택여부 */

	String  evlPlanId;                      /** 평가계획ID */
	String  evlCmitId;                      /** 평가위원회ID */
	String  evlTableId;                     /** 평가표ID */

	String  receiptNo;          			/** 접수번호 */
	String  applyId;                        /** 신청ID */
	String  evlTrgetId;                     /** 평가대상ID */
	String  memberId;                       /** 회원ID */
	String  memberNm;                       /** 회원명 */
	boolean slctn;                          /** 선정여부 */
	String  slctnNm;                        /** 선정여부명*/
	Date    slctnDt;                        /** 선정일시 */
	String  fmtSlctnDt;                     /** 선정일시 포맷팅*/

	Integer addScore;                       /** 평가가점점수 */
	Integer minusScore;                     /** 평가감점점수 */

	String  addRm;                       	/** 평가가점비고 */
	String  minusRm;                     	/** 평가감점비고 */

	Integer topScore; 						/** 최고점수 */
	Integer lwetScore;						/** 최저점수 */
	Integer smScore; 						/** 총점 */
	Integer avrgScore; 						/** 평균점수*/
	Integer gnrlzScore; 					/** 종합점수 */

	@JsonIgnore
	private String creatorId;		/** 생성자 ID */
	@JsonIgnore
	private Date createdDt;			/** 생성일시 */
	@JsonIgnore
	private String updaterId;		/** 수정자 ID */
	@JsonIgnore
	private Date updatedDt;			/** 수정일시 */

	List<EvlResultByMfcmmListDto> evlResultByMfcmmList; /*위원별점수*/

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
