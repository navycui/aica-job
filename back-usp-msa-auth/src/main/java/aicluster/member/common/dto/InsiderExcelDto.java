package aicluster.member.common.dto;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;

public class InsiderExcelDto extends InsiderDto {

	private static final long serialVersionUID = 542592600556847490L;

	public String getEmail() {
		if (string.isBlank(this.getEncEmail())) {
			return null;
		}
		return aes256.decrypt(this.getEncEmail(), this.getMemberId());
	}

	public String getMobileNo() {
		if (string.isBlank(this.getEncMobileNo())) {
			return null;
		}
		return aes256.decrypt(this.getEncMobileNo(), this.getMemberId());
	}

	public String getTelNo() {
		if (string.isBlank(this.getEncTelNo())) {
			return null;
		}
		return aes256.decrypt(this.getEncTelNo(), this.getMemberId());
	}

}
