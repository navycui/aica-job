package aicluster.member.api.insider.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SrchPicItemDto {

	private String memberId;
	private String loginId;
	private String memberNm;
	private String deptNm;
	private String positionNm;
	private String authorityNm;		/** 권한이름 */
	@JsonIgnore
	private String encEmail;

	private Long rn;

	public String getEmail() {
		if (string.isNotBlank(this.encEmail)) {
			return aes256.decrypt(this.encEmail, this.memberId);
		}
		return null;
	}
}
