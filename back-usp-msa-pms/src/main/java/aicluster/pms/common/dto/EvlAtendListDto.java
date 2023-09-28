package aicluster.pms.common.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils;
import lombok.Data;

@Data
public class EvlAtendListDto implements Serializable {
	private static final long serialVersionUID = 736524606919511788L;
	/** 평가위원회ID */
	private String evlMfcmmId;
	/** 평가위원명 */
	private String evlMfcmmNm;
	/** 위원장여부 */
	private Boolean charmn;
	/** 출석상태코드(G:ATEND_STTUS) */
	private String atendSttusCd;
	/** 출석상태명 */
	private String atendSttusNm;
	/** 회피동의여부 */
	private Boolean evasAgreAt;
	/** 소속기관(직장명) */
	private String wrcNm;
	/** 암호화된 비밀번호 */
	@JsonIgnore
	private String encPassword;
	public String getPassword() {
		if(CoreUtils.string.isBlank(this.encPassword)) {
			return "";
		}
		return CoreUtils.aes256.decrypt(this.encPassword, this.evlMfcmmId);
	}
}
