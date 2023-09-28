package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.date;
import lombok.Data;

@Data
public class RsltListDto implements Serializable {
	private static final long serialVersionUID = -4937866877400674887L;
	/** 신청ID */
	private String applyId;
	/** 선정ID */
	private String rsltId;
	/** 사업연도 */
	private String bsnsYear;
	/** 사업명 */
	private String bsnsNm;
	/** 접수번호 */
	private String receiptNo;
	/** 과제명 */
	private String taskNm;
	/** 회원명 */
	private String memberNm;
	/** 회원ID */
	@JsonIgnore
	private String memberId;
	/** 처리상태 */
	private String rsltSttus;
	/** 담당자권한코드(G:CHARGER_AUTHOR) */
	@JsonIgnore
	private String chargerAuthorCd;
	/** 순번 */
	private int rn;
	/** 제출일 */
	@JsonIgnore
	private Date presentnDt;
	public String getPresentnDate() {
		if(this.presentnDt == null) {
			return "";
		}
		return date.format(this.presentnDt, "yyyy-MM-dd");
	}

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

	public Date getPresentnDt() {
		if (this.presentnDt != null) {
			return new Date(this.presentnDt.getTime());
         }
		return null;
	}
	public void setPresentnDt(Date presentnDt) {
		this.presentnDt = null;
		if (presentnDt != null) {
			this.presentnDt = new Date(presentnDt.getTime());
		}
	}
}
