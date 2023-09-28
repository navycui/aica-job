package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.Data;

@Data
public class UsptTaskRspnber implements Serializable {
	/**
	 * 과제책임자
	 */
	private static final long serialVersionUID = -2174401846514848037L;

	/** 과제책임자ID */
	String  taskRspnberId;
	/** 사업계획서ID */
	String  bsnsPlanDocId;
	/** 책임자명 */
	String  rspnberNm;
	 /** 암호화된 생년월일 */
	String  encBrthdy;
	/** 암호화된 휴대폰번호 */
	String  encMbtlnum;
	/** 암호화된 이메일 */
	String  encEmail;
	/** 부서명 */
	String  deptNm;
	/** 직급명 */
	String  clsfNm;
	/** 주소 */
	String  adres;
	/** 암호화된 전화번호 */
	String  encTelno;
	/** 암호화된 팩스번호 */
	String  encFxnum;
	/** 과학기술인등록번호 */
	String  tlsyRegistNo;

	/** 참여기업수 */
	Long    partcptnCompanyCnt;
	/** 중소기업수 */
	Long smlpzCnt;
	/** 중견기업수 */
	Long mspzCnt;
	/** 기타수 */
	Long etcCnt;
	/** 순번 */
	private int rn;

	/** 페이징 처리 */
	@JsonIgnore
	private Long beginRowNum;
	@JsonIgnore
	private Long itemsPerPage;

	/** I:등록, U:수정, D:삭제 */
	private String flag;

	@JsonIgnore
	private String creatorId;		/** 생성자 ID */
	@JsonIgnore
	private Date createdDt;			/** 생성일시 */
	@JsonIgnore
	String  updaterId;              /** 수정자ID(CMMT_MEMBER.MEMBER_ID) */
	@JsonIgnore
	private Date updatedDt;         /** 수정일시 */

	/**
	 * 복호화된 생년월일
	 *
	 * @return
	 */
	public String getBrthdy() {
		if (string.isBlank(this.encBrthdy)) {
			return null;
		}
		return aes256.decrypt(this.encBrthdy, this.taskRspnberId);
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
		return aes256.decrypt(this.encMbtlnum, this.taskRspnberId);
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
		return aes256.decrypt(this.encEmail, this.taskRspnberId);
	}

	/**
	 * 복호화된 전화번호
	 *
	 * @return
	 */
	public String getTelno() {
		if (string.isBlank(this.encTelno)) {
			return null;
		}
		return aes256.decrypt(this.encTelno, this.taskRspnberId);
	}

	/**
	 * 복호화된 팩스번호
	 *
	 * @return
	 */
	public String getFxnum() {
		if (string.isBlank(this.encFxnum)) {
			return null;
		}
		return aes256.decrypt(this.encFxnum, this.taskRspnberId);
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
