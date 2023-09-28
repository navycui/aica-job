package aicluster.framework.common.entity;

import java.io.Serializable;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -4924394213558041398L;
	private String memberId;
	private String memberNm;
	private String nickname;
	private String loginId;
	private String memberType;
	private String gender;
	//private String encBirthday;
	private String pstinstNm;
	private String deptNm;
	private String positionNm;
	private String authorityId;
	private String memberSt;
	private String encEmail;
	private String encMobileNo;
	private String refreshToken;

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
}
