package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UsptExpertClCharger implements Serializable {

	/**
	 * 전문가분류담당자
	 */
	private static final long serialVersionUID = 6899189734815022754L;

	/*전문가분류ID*/
	String expertClId;
	/*회원ID : 담당자ID*/
	String memberId;
	/*담당자명*/
	String memberNm;
	/*부서명*/
	String deptNm;
	/*직급명*/
	String clsfNm;

	/** I:등록, U:수정, D:삭제 */
	private String flag;

	/** 페이징 처리 */
	@JsonIgnore
	private Long beginRowNum;
	@JsonIgnore
	private Long itemsPerPage;

	@JsonIgnore
	private String creatorId;		/** 생성자 ID(CMMT_MEMBER.MEMBER_ID) */
	@JsonIgnore
	private Date createdDt;			/** 생성일시 */
	@JsonIgnore
	private String  updaterId;              /** 수정자ID(CMMT_MEMBER.MEMBER_ID) */
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
