package aicluster.pms.common.dto;

import java.io.Serializable;

import bnet.library.util.CoreUtils;
import lombok.Data;

@Data
public class DspthTrgetListDto implements Serializable {
	private static final long serialVersionUID = -5157298783838644506L;
	/** 회원ID */
	private String memberId;
	/** 회원명 */
	private String memberNm;
	/** 암호화된 이메일 */
	private String encEmail;
	public String getEmail() {
		if(CoreUtils.string.isBlank(this.encEmail)) {
			return "";
		}
		return CoreUtils.aes256.decrypt(this.encEmail, this.memberId);
	}

	/** 암호화된 휴대폰번호 */
	private String encMobileNo;
	public String getMobileNo() {
		if(CoreUtils.string.isBlank(this.encMobileNo)) {
			return "";
		}
		return CoreUtils.aes256.decrypt(this.encMobileNo, this.memberId);
	}
}
