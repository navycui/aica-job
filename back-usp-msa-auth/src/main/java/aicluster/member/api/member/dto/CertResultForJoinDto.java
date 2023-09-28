package aicluster.member.api.member.dto;

import org.apache.commons.lang3.BooleanUtils;

import aicluster.framework.common.dto.SessionKeyDto;
import bnet.library.util.CoreUtils.masking;
import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CertResultForJoinDto extends SessionKeyDto {
	/**
	 *
	 */
	private static final long serialVersionUID = -9206268853440591020L;

	private boolean childYn;				/* 14세 미만 여부 */
	private boolean duplicateYn;			/* 가입여부 */
	private boolean secessionYn;			/* 탈퇴여부 */
	private String  loginId;				/* 가입된 로그인 ID */
	private String  chargerNm;				/* 담당자명 */

	public String getLoginId() {
		if (string.isNotBlank(this.loginId) && BooleanUtils.isTrue(this.duplicateYn)) {
    		// 로그인ID 마스킹 처리(Ai****)
			return masking.maskingLoginId(this.loginId);
		}
		return null;
	}

	public String getChargerNm() {
		if (string.isNotBlank(this.chargerNm) && BooleanUtils.isTrue(this.duplicateYn)) {
    		// 담당자명 마스킹 처리(홍**)
			return masking.maskingName(this.chargerNm);
		}
		return null;
	}
}
