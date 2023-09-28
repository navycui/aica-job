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
public class UsptExpertCareer implements Serializable {

	/**
	 *전문가경력
	 */
	private static final long serialVersionUID = 6798930581626953231L;

	/** 변경 플래그(I,U,D) */
	String  flag;
	/** 전문가경력ID */
	String  expertCareerId;
	/** 전문가ID */
	String  expertId;
	 /** 근무시작일 */
	String  workBgnde;
	/** 근무종료일 */
	String  workEndde;
	/** 직장명 */
	String  wrcNm;
	/** 부서명 */
	String  deptNm;
	/** 직위명 */
	String  ofcpsNm;
	/** 담당업무명 */
	String  chrgJobNm;

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
