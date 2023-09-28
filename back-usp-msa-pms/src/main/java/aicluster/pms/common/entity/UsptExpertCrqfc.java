package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsptExpertCrqfc implements Serializable {

	/**
	 *전문가 자격증정보
	 */
	private static final long serialVersionUID = -4953428188757566301L;

	/** 변경 플래그(I,U,D) */
	String  flag;
	 /** 전문가자격증ID */
	String  expertCrqfcId;
	/** 전문가ID */
	String  expertId;
	 /** 취득일 */
	String  acqdt;
	/** 자격증명 */
	String  crqfcNm;
	 /** 발급기관명 */
	String  issuInsttNm;

	@JsonIgnore
	private String creatorId;		/** 생성자 ID(CMMT_MEMBER.MEMBER_ID) */
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
