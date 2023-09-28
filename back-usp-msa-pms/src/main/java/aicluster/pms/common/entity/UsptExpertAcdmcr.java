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
public class UsptExpertAcdmcr implements Serializable {

	/**
	 *전문가 학력정보
	 */
	private static final long serialVersionUID = -1857822041746695570L;
	/** 변경 플래그(I,U,D) */
	String  flag;
	/** 전문가학력ID */
	String  expertAcdmcrId;
	/** 전문가ID */
	String  expertId;
	/** 학력시작일 */
	String  acdmcrBgnde;
	/** 학력종료일 */
	String  acdmcrEndde;
	/** 학위코드 */
	String  dgriCd;
	/** 부서명 */
	String  deptNm;
	/** 직위명 */
	String  ofcpsNm ;
	/** 담당업무명 */
	String  chrgJobNm;
	/** 학교명 */
	String  schulNm;
	/** 전공명 */
	String  majorNm;
	/** 지도교수명 */
	String  profsrNm;
	/** 졸업구분코드(G:GRDTN_DIV) */
	String  grdtnDivCd;


	@JsonIgnore
	private String creatorId;		  /** 생성자ID(CMMT_MEMBER.MEMBER_ID) */
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
