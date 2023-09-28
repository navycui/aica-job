package aicluster.member.common.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.Data;

@Data
public class MemberSelfDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3179425353930001460L;

	@JsonIgnore
	private String memberId;
	private String loginId;
	private String memberNm;
	private String nickname;
	private String gender;
	private String memberType;
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
	private Date lastLoginDt;

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
