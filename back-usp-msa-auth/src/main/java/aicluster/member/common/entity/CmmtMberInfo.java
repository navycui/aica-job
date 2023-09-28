package aicluster.member.common.entity;

import java.io.Serializable;
import java.util.Date;

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
public class CmmtMberInfo implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -1895824720424371277L;
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
	private String authorityId;
	private String memberSt;
	private Date memberStDt;
	private String blackListBeginDay;
	private String blackListEndDay;
	private String blackListReason;
	private String memberType;
	@JsonIgnore
	private String ci;
	@JsonIgnore
	private String encEmail;
	@JsonIgnore
	private String encBirthday;
	@JsonIgnore
	private String encMobileNo;
	private String chargerNm;
	private String ceoNm;
	private String bizrno;
	private String jurirno;
	private Boolean instr; //강사
	private Boolean marketingReception;
	@JsonIgnore
	private String refreshToken;
	@JsonIgnore
	private String kakaoToken;
	@JsonIgnore
	private String naverToken;
	@JsonIgnore
	private String googleToken;
	private Date lastLoginDt;
	private String creatorId;
	private Date createdDt;
	private String updaterId;
	private Date updatedDt;

	/*
	 * formatting properties
	 */
	private String memberStNm;
	private String authorityNm;
	private String memberTypeNm;

	public String getEmail() {
		if (string.isBlank(this.encEmail)) {
			return null;
		}
		return aes256.decrypt(this.encEmail, this.memberId);
	}

	public String getBirthday() {
		if (string.isBlank(this.encBirthday)) {
			return null;
		}
		return aes256.decrypt(this.encBirthday, this.memberId);
	}

	public String getMobileNo() {
		if (string.isBlank(this.encMobileNo)) {
			return null;
		}
		String mobileNo = aes256.decrypt(this.encMobileNo, this.memberId);
		return mobileNo;
	}
}
