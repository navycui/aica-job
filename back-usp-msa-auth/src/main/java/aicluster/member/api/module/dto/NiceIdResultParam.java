package aicluster.member.api.module.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NiceIdResultParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -6604029340971900113L;
	private String encodeData;	/** 휴대폰 본인인증 결과 암호문 */
	private String sessionId;	/** 약관동의정보 세션ID or 회원가입 인증 세션 모음 세션ID */
}
