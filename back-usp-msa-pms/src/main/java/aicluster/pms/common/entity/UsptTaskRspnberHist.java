package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.Data;

@Data
public class UsptTaskRspnberHist implements Serializable {
	/**
	 * 과제책임자변경이력
	 */
	private static final long serialVersionUID = -8647574999026117702L;
	/** 과제책임자변경이력ID */
	String  taskRspnberHistId;
	/** 협약변경요청ID */
	String  cnvnChangeReqId;
	/** 승인과제책임자변경이력ID */
	String  confmTaskRspnberHistId;
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

	 /*변경일자*/
	String changeDe;
	 /*변경전후*/
	String changeBeAf;

	@JsonIgnore
	private String creatorId;		/** 생성자 ID */
	@JsonIgnore
	private Date createdDt;			/** 생성일시 */

	/**
	 * 복호화된 생년월일
	 *
	 * @return
	 */
	public String getBrthdy() {
		if (string.isBlank(this.encBrthdy)) {
			return null;
		}
		return aes256.decrypt(this.encBrthdy, this.taskRspnberHistId);
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
		return aes256.decrypt(this.encMbtlnum, this.taskRspnberHistId);
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
		return aes256.decrypt(this.encEmail, this.taskRspnberHistId);
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
		return aes256.decrypt(this.encTelno, this.taskRspnberHistId);
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
		return aes256.decrypt(this.encFxnum, this.taskRspnberHistId);
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
