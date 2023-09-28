package aicluster.member.common.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsiderDto implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 7086755507474294553L;
	private String memberId;		/** 회원ID */
	private String loginId;			/** 로그인ID */
	@JsonIgnore
	private String encPasswd;		/** 암호화된 비밀번호 */
	@JsonIgnore
	private Date passwdDt;			/** 비밀번호 변경일시 */
	@JsonIgnore
	private Boolean passwdInit;		/** 비밀번호 초기화여부 */
	private String memberNm;		/** 회원이름 */
	private String nickname;
	@JsonIgnore
	private String gender;
	private String deptNm;
	private String positionNm;
	private String authorityId;		/** 권한ID */
	private String authorityNm;		/** 권한명 */
	private String memberSt;		/** 회원상태 */
	private String memberStNm;		/** 회원상태명 */
	@JsonIgnore
	private Date memberStDt;		/** 회원상태 변경일시 */
	@JsonIgnore
	private String encTelNo;		/** 암호화된 전화번호 */
	@JsonIgnore
	private String encMobileNo;		/** 암호화된 휴대폰번호 */
	@JsonIgnore
	private String encEmail;		/** 암호화된 이메일 */
	@JsonIgnore
	private String memberIps;
	@JsonIgnore
	private String refreshToken;	/** Refresh token */
	@JsonIgnore
	private String creatorId;		/** 생성자ID */
	@JsonIgnore
	private Date createdDt;			/** 생성일시 */
	@JsonIgnore
	private String updaterId;		/** 수정자ID */
	@JsonIgnore
	private Date updatedDt;			/** 수정일시 */
	@JsonIgnore
	private Date lastLoginDt;
	private Long rn;				/** Row 번호 */

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

}
