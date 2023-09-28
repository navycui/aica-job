package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import lombok.Data;

@Data
public class UsptEvlCmitDta implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1948177796263925018L;

	/** 접수번호 */
	String receiptNo;

	/** 회원명 */
	String memberNm;

	/** 평가단계ID */
	String  evlStepId;

	/** 평가단계명 */
	String  evlStepNm;

	/** 평가계획ID */
	String evlPlanId;

	/** 평가대상ID */
	String evlTrgetId;

	/** 첨부파일 그룹ID */
	private String attachmentGroupId;

	/** 생성자 ID */
	@JsonIgnore
	private String creatorId;
	/** 생성일시 */
	@JsonIgnore
	private Date createdDt;
	/** 수정자 ID */
	@JsonIgnore
	private String updaterId;
	/** 수정일시 */
	@JsonIgnore
	private Date updatedDt;

	/** 첨부파일 목록 */
	List<CmmtAtchmnfl> attachmentList;

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
