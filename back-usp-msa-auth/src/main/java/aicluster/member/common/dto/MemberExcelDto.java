package aicluster.member.common.dto;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;

public class MemberExcelDto extends MemberDto {

	private static final long serialVersionUID = -6689093217733826553L;

	public String getEmail() {
		if (string.isBlank(this.getEncEmail())) {
			return null;
		}
		return aes256.decrypt(this.getEncEmail(), this.getMemberId());
	}

	public String getBirthday() {
		if (string.isBlank(this.getEncBirthday())) {
			return null;
		}
		return aes256.decrypt(this.getEncBirthday(), this.getMemberId());
	}

	public String getMobileNo() {
		if (string.isBlank(this.getEncMobileNo())) {
			return null;
		}
		return aes256.decrypt(this.getEncMobileNo(), this.getMemberId());
	}
}
