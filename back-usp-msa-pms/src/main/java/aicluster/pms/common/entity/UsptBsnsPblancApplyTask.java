package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.Data;

@Data
public class UsptBsnsPblancApplyTask implements Serializable {
	private static final long serialVersionUID = -6705116622282187390L;
	/** 신청ID */
	@JsonIgnore
	private String applyId;
	/** 과제명(국문) */
	private String taskNmKo;
	/** 과제명(영문) */
	private String taskNmEn;
	/** 지원분야ID */
	private String applyRealmId;
	/** 지원분야명 */
	private String applyRealmNm;
	/** 책임자 */
	private String rspnberNm;
	/** 암호화된 생년월일 */
	@JsonIgnore
	private String encBrthdy;
	/** 생년월일 */
	private String brthdy;
	/** 암호화된 휴대폰번호 */
	@JsonIgnore
	private String encMbtlnum;
	/** 휴대폰번호 */
	private String mbtlnum;
	/** 암호화된 이메일 */
	@JsonIgnore
	private String encEmail;
	/** 이메일 */
	private String email;
	/** 부서/학과명 */
	private String deptNm;
	/** 직위/직급명 */
	private String ofcpsNm;
	/** 주소 */
	private String adres;
	/** 암호화된 전화번호 */
	@JsonIgnore
	private String encTelno;
	/** 전화번호 */
	private String telno;
	/** 암호화된 팩스번호 */
	@JsonIgnore
	private String encFxnum;
	/** 팩스번호 */
	private String fxnum;
	/** 과학기술인등록번호 */
	private String sctecrno;
	/** 희망직무코드(G:HOPE_DTY) */
	private String hopeDtyCd;
	/** 희망직무 */
	private String hopeDty;
	/** 현주소 */
	private String nowAdres;
	/** 최근 발송일 */
	private Date recentSendDate;
	/** 과제유형코드 */
	private String taskTypeCd;

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

	@JsonIgnore
	public String getDecFxnum() {
		if (string.isBlank(this.encFxnum)) {
			return null;
		}
		return aes256.decrypt(this.encFxnum, this.applyId);
	}

	@JsonIgnore
	public String getDecTelno() {
		if (string.isBlank(this.encTelno)) {
			return null;
		}
		return aes256.decrypt(this.encTelno, this.applyId);
	}

	@JsonIgnore
	public String getDecBrthdy() {
		if (string.isBlank(this.encBrthdy)) {
			return null;
		}
		return aes256.decrypt(this.encBrthdy, this.applyId);
	}

	@JsonIgnore
	public String getDecMbtlnum() {
		if (string.isBlank(this.encMbtlnum)) {
			return null;
		}
		return aes256.decrypt(this.encMbtlnum, this.applyId);
	}

	@JsonIgnore
	public String getDecEmail() {
		if (string.isBlank(this.encEmail)) {
			return null;
		}
		return aes256.decrypt(this.encEmail, this.applyId);
	}

	public Date getRecentSendDate() {
		if (this.recentSendDate != null) {
			return new Date(this.recentSendDate.getTime());
         }
		return null;
	}
	public void setRecentSendDate(Date recentSendDate) {
		this.recentSendDate = null;
		if (recentSendDate != null) {
			this.recentSendDate = new Date(recentSendDate.getTime());
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
