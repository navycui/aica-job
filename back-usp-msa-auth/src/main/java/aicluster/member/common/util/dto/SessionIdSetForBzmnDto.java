package aicluster.member.common.util.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionIdSetForBzmnDto implements Serializable {

	private static final long serialVersionUID = 6059640729980155133L;

    private String termsConsentSessionId;	/** 약관동의DTO가 저장된 Session ID */
    private String pkiCertSessionId;		/** 사업자회원 공동인증서 인증 결과 DTO가 저장된 Session ID */
    private String phoneCertSessionId;		/** 휴대폰번호 인증번호 요청 Session ID */
    private String emailCertSessionId;		/** 이메일 인증번호 요청 Session ID */
}
