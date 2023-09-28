package aicluster.common.common.dto;

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
public class CreatrInfDto implements Serializable {

	private static final long serialVersionUID = -5188206396180941377L;

	@JsonIgnore
	private String memberId;
	private String loginId;
	private String memberNm;
	private String memberType;
	private String memberTypeNm;
	private String positionNm;
	private String chargerNm;
	@JsonIgnore
	private String encEmail;
	@JsonIgnore
	private String encMobileNo;

	public String getEmail() {
		if (string.isBlank(this.encEmail)) {
			return null;
		}
		return aes256.decrypt(this.encEmail, this.memberId);
	}

	public String getMobileNo() {
		if (string.isBlank(this.encMobileNo)) {
			return null;
		}
		return aes256.decrypt(this.encMobileNo, this.memberId);
	}
}
