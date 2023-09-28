package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UsptBsnsPblancApplcnt implements Serializable {
	private static final long serialVersionUID = -7301351789062415751L;
	/** 신청ID */
	private String applyId;
	/** 공고ID */
	private String pblancId;
	/** 접수차수 */
	private Integer rceptOdr;
	/** 접수번호(BA + 8자리 순번) */
	private String receiptNo;
	/** 회원ID */
	private String memberId;
	/** 회원명 */
	private String memberNm;
	/** 접수일시 */
	private Date rceptDt;
	/** 접수상태코드(G:RCEPT_STTUS) */
	private String rceptSttusCd;
	/** 접수상태 */
	private String rceptSttus;
	/** 접수상태 변경일시 */
	@JsonIgnore
	private Date rceptSttusDt;
	/** 보완의견내용 */
	private String makeupOpinionCn;
	/** 보완요청내용 */
	private String makeupReqCn;
	/** 반려사유내용 */
	private String rejectReasonCn;
	/** 선정여부 */
	private Boolean slctn;
	/** 선정일시 */
	private Date slctnDt;
	/** 회원유형 */
	private String memberType;
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

	public Date getRceptDt() {
		if (this.rceptDt != null) {
			return new Date(this.rceptDt.getTime());
         }
		return null;
	}
	public void setRceptDt(Date rceptDt) {
		this.rceptDt = null;
		if (rceptDt != null) {
			this.rceptDt = new Date(rceptDt.getTime());
		}
	}

	public Date getRceptSttusDt() {
		if (this.rceptSttusDt != null) {
			return new Date(this.rceptSttusDt.getTime());
         }
		return null;
	}
	public void setRceptSttusDt(Date rceptSttusDt) {
		this.rceptSttusDt = null;
		if (rceptSttusDt != null) {
			this.rceptSttusDt = new Date(rceptSttusDt.getTime());
		}
	}

	public Date getSlctnDt() {
		if (this.slctnDt != null) {
			return new Date(this.slctnDt.getTime());
         }
		return null;
	}
	public void setSlctnDt(Date slctnDt) {
		this.slctnDt = null;
		if (slctnDt != null) {
			this.slctnDt = new Date(slctnDt.getTime());
		}
	}

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
