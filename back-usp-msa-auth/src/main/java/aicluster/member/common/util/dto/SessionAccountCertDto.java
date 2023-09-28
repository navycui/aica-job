package aicluster.member.common.util.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionAccountCertDto implements Serializable {/**
	 *
	 */
	private static final long serialVersionUID = -2760472992905425079L;

	private boolean isIndividual;	/** 개인회원여부(true:개인회원, false:사업자회원) */
	private String memberId;		/** 계정 회원ID */
	private String loginId;			/** 계정 로그인 ID */
	private String ci;				/** 연계정보 확인값 (Connect infomation) */
	private String mobileNo;        /** 휴대폰번호 (휴대폰 본인인증 결과정보) */
	private String bizrno;			/** 사업자등록번호 (공동인증서 인증 결과정보) */
}
