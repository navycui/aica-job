package aicluster.member.api.insider.dto;

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
public class InsiderMemberDto {

	private String memberId;
	private String loginId;
	private String memberNm;
	private String deptNm;
	private String positionNm;
	private String authorityId;
	private String memberSt;
	private String telNo;
	private String mobileNo;
	private String email;
	private String memberIps;

	public String getEncTelNo() {
		if (string.isBlank(this.telNo)) {
			return null;
		}
		return aes256.encrypt(this.telNo, this.memberId);
	}
	public String getEncMobileNo() {
		if (string.isBlank(this.mobileNo)) {
			return null;
		}
		return aes256.encrypt(this.mobileNo, this.memberId);
	}

	public String getEncEmail() {
		if (string.isBlank(this.email)) {
			return null;
		}
		return aes256.encrypt(this.email, this.memberId);
	}

}
