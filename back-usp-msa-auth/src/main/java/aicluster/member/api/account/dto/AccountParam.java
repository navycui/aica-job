package aicluster.member.api.account.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -9185427245727934317L;

	private String sessionId;		/** 계정 인증 결과 Session ID */
}
