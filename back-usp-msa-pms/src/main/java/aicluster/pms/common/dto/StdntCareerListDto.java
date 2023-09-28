package aicluster.pms.common.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils;
import lombok.Data;

@Data
public class StdntCareerListDto implements Serializable {
	private static final long serialVersionUID = 5044012852364097699L;
	/** 신청ID */
	private String applyId;
	/** 접수번호 */
	private String receiptNo;
	/** 희망직무 */
	private String hopeDty;
	/** 이름 */
	private String memberNm;
	/** 매칭여부 */
	private String mtchAt;
	/** 매칭기관 */
	private String mtchInstt;
	/** 사업연도 */
	private String bsnsYear;
	/** 공고명 */
	private String pblancNm;
	/** 순번 */
	private Long rn;
	/** 담당자권한코드 */
	@JsonIgnore
	private String chargerAuthorCd;

	/** 회원ID */
	@JsonIgnore
	private String memberId;
	/** 암호화된 이메일 */
	@JsonIgnore
	private String encEmail;
	@JsonIgnore
	public String getEmail() {
		if(CoreUtils.string.isBlank(this.encEmail)) {
			return "";
		}
		return CoreUtils.aes256.decrypt(this.encEmail, this.memberId);
	}

	/** 암호화된 휴대폰번호 */
	@JsonIgnore
	private String encMobileNo;
	@JsonIgnore
	public String getMobileNo() {
		if(CoreUtils.string.isBlank(this.encMobileNo)) {
			return "";
		}
		return CoreUtils.aes256.decrypt(this.encMobileNo, this.memberId);
	}

}
