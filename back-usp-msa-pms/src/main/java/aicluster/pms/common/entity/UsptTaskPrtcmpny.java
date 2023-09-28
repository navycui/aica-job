package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.Data;

@Data
public class UsptTaskPrtcmpny implements Serializable {
	private static final long serialVersionUID = -7130368840052711674L;
	/** 과제참여기업ID */
	private String taskPartcptnEntrprsId;
	/** 사업계획서ID */
	private String bsnsPlanDocId;
	/** 업체명 */
	private String entrpsNm;
	/** 책임자명 */
	private String rspnberNm;
	/** 직급명 */
	private String clsfNm;
	/** 암호화된 전화번호 */
	@JsonIgnore
	private String encTelno;
	/** 암호화된 휴대폰번호 */
	@JsonIgnore
	private String encMbtlnum;
	/** 암호화된 이메일 */
	@JsonIgnore
	private String encEmail;
	/** 국가연구자번호 */
	private String tlsyRegistNo;
	/** 최근 발송일 */
	private String recentSendDate;


	/** 등록전화번호 */
	private String regTelno;
	/** 등록휴대폰번호 */
	private String regMbtlnum;
	/** 등록이메일 */
	private String regEmail;

	/** 회원ID : 소속업체회원ID */
	private String memberId;
	/** 회원명(사업자명) */
	private String memberNm;

	/** 참여기업수 */
	Long    partcptnCompanyCnt;
	 /** 중소기업수 */
	Long    smlpzCnt;
	 /** 중견기업수 */
	Long    mspzCnt;
	 /** 기타수 */
	Long    etcCnt;

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

	/** I:등록, U:수정, D:삭제 */
	private String flag;

	/**
	 * 복호화된 전화번호
	 *
	 * @return
	 */
	public String getTelno() {
		if (string.isBlank(this.encTelno)) {
			return null;
		}
		return aes256.decrypt(this.encTelno, this.taskPartcptnEntrprsId);
	}

	/**
	 * 복호화된 휴대폰번호
	 *
	 * @return
	 */
	public String getMbtlnum() {
		if (string.isBlank(this.encMbtlnum)) {
			return null;
		}
		return aes256.decrypt(this.encMbtlnum, this.taskPartcptnEntrprsId);
	}

	/**
	 * 복호화된 이메일
	 *
	 * @return
	 */
	public String getEmail() {
		if (string.isBlank(this.encEmail)) {
			return null;
		}
		return aes256.decrypt(this.encEmail, this.taskPartcptnEntrprsId);
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
