package aicluster.member.common.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmmtEmpInfo implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 8553614540853172374L;
	private String memberId;
	private String loginId;
	@JsonIgnore
	private String encPasswd;
	@JsonIgnore
	private Date passwdDt;
	@JsonIgnore
	private Boolean passwdInit;
	private String memberNm;
	private String nickname;
	private String gender;
	private String deptNm;
	private String positionNm;
	private String authorityId;
	private String authorityNm;		/** 권한명 */
	private String memberSt;
	private String memberStNm;
	private Date memberStDt;
	@JsonIgnore
	private String encTelNo;
	@JsonIgnore
	private String encMobileNo;
	@JsonIgnore
	private String encEmail;
	private String memberIps;
	@JsonIgnore
	private String refreshToken;
	private String creatorId;
	private Date createdDt;
	private String updaterId;
	private Date updatedDt;
	@JsonIgnore
	private Date lastLoginDt;

	@Override
	public int hashCode() {
		return Objects.hash(authorityId, deptNm, encEmail, encTelNo, encMobileNo, encPasswd, gender, loginId,
				memberId, memberIps, memberNm, memberSt, nickname, positionNm);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CmmtEmpInfo other = (CmmtEmpInfo) obj;
		return Objects.equals(authorityId, other.authorityId) && Objects.equals(deptNm, other.deptNm)
				&& Objects.equals(encEmail, other.encEmail)	&& Objects.equals(encMobileNo, other.encMobileNo)
				&& Objects.equals(encPasswd, other.encPasswd) && Objects.equals(gender, other.gender)
				&& Objects.equals(loginId, other.loginId) && Objects.equals(memberId, other.memberId)
				&& Objects.equals(memberIps, other.memberIps) && Objects.equals(memberNm, other.memberNm)
				&& Objects.equals(memberSt, other.memberSt) && Objects.equals(nickname, other.nickname)
				&& Objects.equals(positionNm, other.positionNm) && Objects.equals(encTelNo, other.encTelNo);
	}

	public String getEmail() {
		if (string.isBlank(this.encEmail)) {
			return null;
		}
		return aes256.decrypt(this.encEmail, this.memberId);
	}

	public String getMobileNo() {
		if (string.isBlank(this.encMobileNo)) {
			return null;
		}
		return aes256.decrypt(this.encMobileNo, this.memberId);
	}

	public String getTelNo() {
		if (string.isBlank(this.encTelNo)) {
			return null;
		}
		return aes256.decrypt(this.encTelNo, this.memberId);
	}
	
	public String getMemberIps() {
		// 회원IP에 대한 equals 연산을 위해 NULL인 경우 "" 출력
		if (string.isEmpty(this.memberIps)) {
			return "";
		}
		return this.memberIps;
	}
}
