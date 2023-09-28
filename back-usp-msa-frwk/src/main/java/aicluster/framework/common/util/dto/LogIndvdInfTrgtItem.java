package aicluster.framework.common.util.dto;

import java.io.Serializable;

import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import bnet.library.util.CoreUtils.validation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogIndvdInfTrgtItem implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2983443308657521497L;

	private String trgterId;	/** 대상자ID */
	private String trgterNm;	/** 대상자명 */
	private String email;		/** 이메일 */
	private String birthday;	/** 생년월일(yyyyMMdd) */
	private String mobileNo;	/** 휴대폰번호 */

	/**
	 * 이메일을 마스킹하여 리턴
	 *
	 * @return 마스킹된 이메일(te**@atops.or.kr)
	 */
	public String getMaskingEmail() {
		if ( string.isBlank(this.email) || !validation.isEmail(this.email) ) {
			return null;
		}
		return CoreUtils.masking.maskingEmail(this.email);
	}

	/**
	 * 생년월일을 마스킹하여 리턴
	 *
	 * @return 마스킹된 생년월일(****.**.**)
	 */
	public String getMaskingBirthday() {
		if ( string.isBlank(this.birthday) || !date.isValidDate(this.birthday, "yyyyMMdd") ) {
			return null;
		}
		return CoreUtils.masking.maskingBirthday(this.birthday, ".");
	}

	/**
	 * 휴대폰 번호를 마스킹하여 리턴
	 *
	 * @return 마스킹된 휴대폰 번호(010-1**4-****)
	 */
	public String getMaskingMobileNo() {
		if ( string.isBlank(this.mobileNo) ) {
			return null;
		}
		return CoreUtils.masking.maskingMobileNo(this.mobileNo);
	}

	public String getEncEmail() {
		if ( string.isBlank(this.email) || !validation.isEmail(this.email) ) {
			return null;
		}
		return CoreUtils.aes256.encrypt(this.email, this.trgterId);
	}

	public String getEncBirthday() {
		if ( string.isBlank(this.birthday) || !date.isValidDate(this.birthday, "yyyyMMdd") ) {
			return null;
		}
		return CoreUtils.aes256.encrypt(this.birthday, this.trgterId);
	}

	public String getEncMobileNo() {
		if ( string.isBlank(this.mobileNo) ) {
			return null;
		}
		return CoreUtils.aes256.encrypt(this.mobileNo, this.trgterId);
	}
}
