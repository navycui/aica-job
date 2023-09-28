package aicluster.framework.common.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogtIndvdlinfoDwldTrget implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4784290150947241834L;

	private String logId;			/** 로그ID */
	private String trgterId;		/** 대상자ID */
	private String trgterNm;		/** 대상자명 */
	@JsonIgnore
	private String encptEmail;		/** 이메일 */
	@JsonIgnore
	private String encptBrthdy;		/** 생년월일(yyyyMMdd) */
	@JsonIgnore
	private String encptMbtlnum;	/** 휴대폰번호 */

	/**
	 * 암호화된 이메일을 복호화 리턴
	 *
	 * @return 이메일
	 */
	public String getEmail() {
		if ( string.isBlank(this.encptEmail) ) {
			return null;
		}

		return aes256.decrypt(this.encptEmail, this.trgterId);
	}

	/**
	 * 암호화된 생년월일을 복호화 리턴
	 *
	 * @return 생년월일
	 */
	public String getBirthday() {
		if ( string.isBlank(this.encptBrthdy) ) {
			return null;
		}

		return aes256.decrypt(this.encptBrthdy, this.trgterId);
	}

	/**
	 * 암호화된 휴대폰 번호를 복호화 리턴
	 *
	 * @return 휴대폰 번호
	 */
	public String getMobileNo() {
		if ( string.isBlank(this.encptMbtlnum) ) {
			return null;
		}

		return aes256.decrypt(this.encptMbtlnum, this.trgterId);
	}
}
