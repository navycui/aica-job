package aicluster.member.common.entity;

import java.io.Serializable;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CmmvUser implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -4773395193394502308L;
	private String memberId;
	private String memberNm;
	private String nickname;
	private String loginId;
	private String memberType;
	private String gender;
	private String encBirthday;
	private String deptNm;
	private String positionNm;
	private String authorityId;
	private String memberSt;
	private String encEmail;
	private String encMobileNo;
	private String refreshToken;

	public String getBirthday() {
		if (string.isBlank(encBirthday)) {
			return null;
		}
		return aes256.decrypt(encBirthday, memberId);
	}

	public String getEmail() {
		if (string.isBlank(encEmail)) {
			return null;
		}
		return aes256.decrypt(encEmail, memberId);
	}

	public String getMobileNo() {
		if (string.isBlank(encMobileNo)) {
			return null;
		}
		return aes256.decrypt(encMobileNo, memberId);
	}
}
