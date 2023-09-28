package aicluster.member.common.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -8111345651157538789L;
	private String memberId;		/** 회원 ID */
	private String loginId;			/** 로그인 ID */
	@JsonIgnore
	private String encPasswd;		/** 암호화된 비밀번호 */
	@JsonIgnore
	private Date passwdDt;			/** 비밀번호 변경일시 */
	@JsonIgnore
	private Boolean passwdInit;		/** 비밀번호 초기여부 */
	private String memberNm;		/** 회원 이름 */
	private String authorityId;		/** 권한 ID */
	private String authorityNm;		/** 권한이름 */
	private String memberSt;		/** 회원상태 */
	private String memberStNm;		/** 회원상태이름 */
	@JsonIgnore
	private Date memberStDt;		/** 회원상태변경일시 */
	private String memberType;		/** 회원구분 */
	private String memberTypeNm;	/** 회원구분이름 */
	@JsonIgnore
	private String ci;				/** 공인인증서 고유식별값 */
	@JsonIgnore
	private String encEmail;		/** 암호화된 이메일 */
	@JsonIgnore
	private String encBirthday;		/** 암호화된 생년월일(개인회원) */
	@JsonIgnore
	private String encMobileNo;		/** 암호화된 휴대폰번호 */
	private String ceoNm;			/** 대표자명 */
	private String bizrno;			/** 사업자번호(기업회원) */
	private boolean instr;			/** 강사여부 */
	@JsonIgnore
	private String refreshToken;	/** Refresh token */
	@JsonIgnore
	private String creatorId;		/** 생성자 ID */
	private Date createdDt;			/** 생성일시 */
	@JsonIgnore
	private String updaterId;		/** 수정자 ID */
	@JsonIgnore
	private Date updatedDt;			/** 수정일시 */
	private Long rn;				/** Row 번호 */

	public String getInstrNm() {
		if (this.instr == true) {
			return "강사";
		}
		else {
			return "강사아님";
		}
	}
	public String getFmtCreatedDt() {
		if (this.createdDt == null) {
			return null;
		}
		return date.format(this.createdDt, "yyyy-MM-dd HH:mm:ss");
	}
	public String getFmtCreatedDay() {
		if (this.createdDt == null) {
			return null;
		}
		return date.format(this.createdDt, "yyyy-MM-dd");
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


}
