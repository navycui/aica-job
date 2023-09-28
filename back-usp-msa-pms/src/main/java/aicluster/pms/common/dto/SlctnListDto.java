package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils;
import lombok.Data;

@Data
public class SlctnListDto implements Serializable {
	private static final long serialVersionUID = -8421035341495581477L;
	/** 평가대상ID */
	private String evlTrgetId;
	/** 신청ID */
	private String applyId;
	/** 분과ID */
	private String sectId;
	/** 평가단계ID */
	private String evlStepId;
	/** 선정여부  */
	private Boolean slctn;
	/** 선정일시 */
	private Date slctnDt;
	/** 회원ID */
	private String memberId;
	/** 회원명 */
	@JsonIgnore
	private String memberNm;
	/** 암호화이메일 */
	@JsonIgnore
	private String encEmail;
	/** 암호화 전화번호 */
	@JsonIgnore
	private String encMobileNo;

	/** 이메일 */
	@JsonIgnore
	public String getEmail() {
		if(this.encEmail == null) {
			return "";
		}
		return CoreUtils.aes256.decrypt(this.encEmail, this.memberId);
	}
	/** 전화번호 */
	@JsonIgnore
	public String getMobileNo() {
		if(this.encMobileNo == null) {
			return "";
		}
		return CoreUtils.aes256.decrypt(this.encMobileNo, this.memberId);
	}


	public Date getSlctnDt() {
		if (this.slctnDt != null) {
			return new Date(this.slctnDt.getTime());
         }
		return null;
	}
	public void setSlctnDt(Date slctnDt) {
		this.slctnDt = null;
		if (slctnDt != null) {
			this.slctnDt = new Date(slctnDt.getTime());
		}
	}
}
