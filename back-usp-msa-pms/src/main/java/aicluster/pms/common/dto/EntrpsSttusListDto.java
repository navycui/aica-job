package aicluster.pms.common.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.Data;

@Data
public class EntrpsSttusListDto implements Serializable {
	private static final long serialVersionUID = -3583133157971117486L;
	private String applyId;
	/** 사업코드 */
	private String bsnsCd;
	/** 사업명 */
	private String bsnsNm;
	/** 사업년도 */
	private String bsnsYear;
	/** 공고ID */
	private String pblancId;
	/** 공고명 */
	private String pblancNm;
	/** 과제명 */
	private String taskNm;
	/** 과제유형 */
	private String taskType;
	/** 참여구분 */
	private String partcptnDiv;
	/** 업체명 */
	private String entrpsNm;
	/** 회원ID */
	@JsonIgnore
	private String memberId;
	/** 사업자등록번호 */
	private String bizrno;
	/** 담당자 */
	private String rspnberNm;
	/** 휴대폰번호 */
	@JsonIgnore
	private String encMbtlnum;
	/** 이메일 */
	@JsonIgnore
	private String encEmail;
	/** 최근발송일 */
	private String recentSendDate;
	/** 과제참여기업ID */
	private String taskPartcptnEntrprsId;
	/** 순번 */
	private Integer rn;

	/** 이메일 */
	@JsonIgnore
	public String getEmail() {
		if (string.isBlank(this.encEmail)) {
			return "";
		}
		if(CoreUtils.string.isBlank(this.taskPartcptnEntrprsId)) {
			return aes256.decrypt(this.encEmail, this.applyId);
		} else {
			return aes256.decrypt(this.encEmail, this.taskPartcptnEntrprsId);
		}
	}
	/** 휴대폰번호 */
	@JsonIgnore
	public String getMbtlnum() {
		if (string.isBlank(this.encMbtlnum)) {
			return "";
		}
		if(CoreUtils.string.isBlank(this.taskPartcptnEntrprsId)) {
			return aes256.decrypt(this.encMbtlnum, this.applyId);
		} else {
			return aes256.decrypt(this.encMbtlnum, this.taskPartcptnEntrprsId);
		}
	}

	/** 마스킹 이메일 */
	public String getMaskingEmail() {
		String decryptEmail = this.getEmail();
		if(CoreUtils.string.isBlank(decryptEmail) || decryptEmail.length() < 5) {
			return "";
		}
		return CoreUtils.masking.maskingEmail(decryptEmail);
	}

	/** 마스킹 휴대폰번호 */
	public String getMaskingMbtlnum() {
		String decryptMbtlnum = this.getMbtlnum();
		if(CoreUtils.string.isBlank(decryptMbtlnum) || decryptMbtlnum.length() < 5) {
			return "";
		}
		return CoreUtils.masking.maskingMobileNo(decryptMbtlnum);
	}
}
