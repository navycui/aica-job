package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmmtInsider implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2857322103696187012L;

	@JsonIgnore
	private String  memberId     ;                  /** 회원ID */
	private String  loginId      ;                  /** 로그인ID */
	@JsonIgnore
	private String  encPasswd    ;                  /** 암호화된 비밀번호 */
	@JsonIgnore
	private Date    passwdDt     ;                  /** 비밀번호 변경일시 */
	@JsonIgnore
	private Boolean passwdInit   ;                  /** 비밀번호 초기화여부 */
	private String  memberNm     ;                  /** 회원명(사업자명) */
	@JsonIgnore
	private String  nickname     ;                  /** 닉네임 */
	@JsonIgnore
	private String  gender       ;                  /** 성별(G:GENDER) */
	private String  deptNm       ;                  /** 부서명 */
	private String  positionNm   ;                  /** 직급 */
	@JsonIgnore
	private String  authorityId  ;                  /** 권한ID */
	@JsonIgnore
	private String  memberSt     ;                  /** 회원상태(G:MEMBER_ST) */
	@JsonIgnore
	private Date    memberStDt   ;                  /** 회원상태변경일시 */
	@JsonIgnore
	private String  encTelNo     ;
	@JsonIgnore
	private String  encMobileNo  ;                  /** 암호화된 휴대폰번호 */
	@JsonIgnore
	private String  encEmail     ;                  /** 암호화된 이메일 */
	@JsonIgnore
	private String  memberIps    ;                  /** 회원IP('/'로 구분) */
	@JsonIgnore
	private String  refreshToken ;                  /** Refresh token */
	@JsonIgnore
	private String  creatorId    ;                  /** 생성자ID(CMMT_MEMBER.MEMBER_ID) */
	@JsonIgnore
	private Date    createdDt    ;                  /** 생성일시 */
	@JsonIgnore
	private String  updaterId    ;                  /** 수정자ID(CMMT_MEMBER.MEMBER_ID) */
	@JsonIgnore
	private Date    updatedDt    ;                  /** 수정일시 */
	@JsonIgnore
	private Date    lastLoginDt  ;                  /** 마지막 로그인 일시 */

	public String getEmail() {
		if (string.isBlank(this.encEmail)) {
			return null;
		}
		return aes256.decrypt(this.encEmail, this.memberId);
	}

//	public String getMobileNo() {
//		if (string.isBlank(this.encMobileNo)) {
//			return null;
//		}
//		return aes256.decrypt(this.encMobileNo, this.memberId);
//	}

	public String getTelNo() {
		if (string.isBlank(this.encTelNo)) {
			return null;
		}
		return aes256.decrypt(this.encTelNo, this.memberId);
	}

	public Date getPasswdDt() {
		if (this.passwdDt != null) {
			return new Date(this.passwdDt.getTime());
         }
		return null;
	}
	public void setPasswdDt(Date passwdDt) {
		this.passwdDt = null;
		if (passwdDt != null) {
			this.passwdDt = new Date(passwdDt.getTime());
		}
	}

	public Date getMemberStDt() {
		if (this.memberStDt != null) {
			return new Date(this.memberStDt.getTime());
         }
		return null;
	}
	public void setMemberStDt(Date memberStDt) {
		this.memberStDt = null;
		if (memberStDt != null) {
			this.memberStDt = new Date(memberStDt.getTime());
		}
	}

	public Date getLastLoginDt() {
		if (this.lastLoginDt != null) {
			return new Date(this.lastLoginDt.getTime());
         }
		return null;
	}
	public void setLastLoginDt(Date lastLoginDt) {
		this.lastLoginDt = null;
		if (lastLoginDt != null) {
			this.lastLoginDt = new Date(lastLoginDt.getTime());
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
