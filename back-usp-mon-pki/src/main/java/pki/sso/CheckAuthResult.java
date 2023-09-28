package pki.sso;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckAuthResult implements Serializable {
	private static final long serialVersionUID = -5289573310457880835L;
	String agentId;
	String resultCode;
	String resultMessage;
	String secureSessionId;
	String returnUrl;
	String loginId;
}
