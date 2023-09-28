package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.Data;

@Data
public class UsptTaskPrtcmpnyHist implements Serializable {
	/**
	 * 과제참여기업변경이력
	 */
	private static final long serialVersionUID = -6735169738889689372L;

	 /** 과제참여기업변경이력ID */
	String  taskPartcptnEntrprsHistId;
	/** 과제참여기업정보변경이력ID */
	String  taskPrtcmpnyInfoHistId;
	/** 업체명 */
	String  entrpsNm;
	/** 책임자명 */
	String  rspnberNm;
	/** 직급명 */
	String  clsfNm;
	/** 암호화된 전화번호 */
	String  encTelno;
	/** 암호화된 휴대폰번호 */
	String  encMbtlnum;
	/** 암호화된 이메일 */
	String  encEmail;
	 /** 과학기술인등록번호 */
	String  tlsyRegistNo;
	/** 회원ID */
	String memberId;

	/** 생성자ID */
	@JsonIgnore
	private String creatorId;
	/** 생성일시 */
	@JsonIgnore
	private Date createdDt;


	/**
	 * 복호화된 전화번호
	 *
	 * @return
	 */
	public String getTelno() {
		if (string.isBlank(this.encTelno)) {
			return null;
		}
		return aes256.decrypt(this.encTelno, this.taskPartcptnEntrprsHistId);
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
		return aes256.decrypt(this.encMbtlnum, this.taskPartcptnEntrprsHistId);
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
		return aes256.decrypt(this.encEmail, this.taskPartcptnEntrprsHistId);
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
}
