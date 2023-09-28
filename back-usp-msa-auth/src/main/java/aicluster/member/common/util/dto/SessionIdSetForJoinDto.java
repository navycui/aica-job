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
public class SessionIdSetForJoinDto implements Serializable {

    private static final long serialVersionUID = 5788386813238929402L;

    private boolean childYn;                 /* 14세 미만 여부 */
    private String termsConsentSessionId;     /* 약관동의DTO가 저장된 Session ID */
    private String mobileCertSessionId;       /* 개인회원 휴대본 본인인증 결과 DTO가 저장된 Session ID */
    private String pkiCertSessionId;          /* 사업자회원 공동인증서 인증 결과 DTO가 저장된 Session ID */
    private String parentMobileCertSessionId; /* 개인회원 가입시 14세 미만인 경우 부모 휴대폰 본인인증 결과 DTO가 저장된 Session ID */
}
