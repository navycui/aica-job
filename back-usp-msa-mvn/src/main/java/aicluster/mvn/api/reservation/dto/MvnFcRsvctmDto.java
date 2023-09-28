package aicluster.mvn.api.reservation.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.masking;
import bnet.library.util.CoreUtils.string;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MvnFcRsvctmDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -6122283270304339569L;

	@JsonIgnore
	private String memberId;
	private String memberNm;
	private String nickname;
	private String loginId;
	@JsonIgnore
	private String encEmail;
	@JsonIgnore
	private String encMobileNo;
	private Boolean isMvn;		/** 입주여부(true:입주, false:미입주) */
	private String bnoRoomNm;	/** 입주호실 */

    public String getEmail() {
        if (string.isBlank(this.encEmail)) {
            return null;
        }
        // 이메일 복호화 및 마스킹
        return masking.maskingEmail(aes256.decrypt(this.encEmail, this.memberId));
    }

    public String getMobileNo() {
        if (string.isBlank(this.encMobileNo)) {
            return null;
        }
        // 휴대폰번호 복호화 및 마스킹
        return masking.maskingMobileNo(aes256.decrypt(this.encMobileNo, this.memberId));
    }
}
