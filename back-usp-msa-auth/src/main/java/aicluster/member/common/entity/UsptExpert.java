package aicluster.member.common.entity;

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
public class UsptExpert implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 4236536432135154931L;
	String  expertId;                       /** 전문가ID */
	String  expertNm;                       /** 전문가명 */
	String  encBrthdy;                      /** 암호화된 생년월일 */
	String  encMbtlnum;                     /** 암호화된 휴대폰번호 */
	String  encEmail;                       /** 암호화된 이메일 */
	String  expertReqstProcessSttusCd;      /** 전문가신청처리상태코드(G:EXPERT_REQST_PROCESS_STTUS) */

	/**
	 * 복호화된 생년월일
	 *
	 * @return
	 */
	public String getBrthdy() {
		if (string.isBlank(this.encBrthdy)) {
			return null;
		}
		return aes256.decrypt(this.encBrthdy, this.expertId);
	}

	/**
	 * 복호화된 휴대폰번호
	 *
	 * @return
	 */
	public String getMbtlnum() {
		if (string.isBlank(this.encMbtlnum)) {
			return null;
		}
		return aes256.decrypt(this.encMbtlnum, this.expertId);
	}

	/**
	 * 복호화된 이메일
	 *
	 * @return
	 */
	public String getEmail() {
		if (string.isBlank(this.encEmail)) {
			return null;
		}
		return aes256.decrypt(this.encEmail, this.expertId);
	}
}
