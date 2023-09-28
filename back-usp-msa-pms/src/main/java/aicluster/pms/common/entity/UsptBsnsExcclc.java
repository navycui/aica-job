package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UsptBsnsExcclc implements Serializable {
	private static final long serialVersionUID = 3979428684824712236L;
	/** 사업정산ID */
	private String bsnsExcclcId;
	/** 사업협약ID */
	private String bsnsCnvnId;
	/** 과제신청사업비ID */
	private String taskReqstWctId;
	/** 집행보조금 */
	private Long excutSbsidy;
	/** 집행사업자부담금 */
	private Long excutBsnmAlotm;
	/** 집행협약총액 */
	private Long excutCnvnTotamt;
	/** 첨부파일그룹ID */
	private String attachmentGroupId;
	/** 생성자ID */
	@JsonIgnore
	private String creatorId;
	/** 생성일시 */
	@JsonIgnore
	private Date createdDt;
	/** 수정자ID */
	@JsonIgnore
	private String updaterId;
	/** 수정일시 */
	@JsonIgnore
	private Date updatedDt;

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
