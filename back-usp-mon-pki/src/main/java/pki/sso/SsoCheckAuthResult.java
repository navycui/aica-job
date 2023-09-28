package pki.sso;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SsoCheckAuthResult implements Serializable {
	private static final long serialVersionUID = -3447929391228711139L;
	String resultCode;
	String resultMessage;
	String secureToken;
	String returnUrl;
	String useISignPage;
	String nextAuthType;
	String useCSMode;
	String useNotesMode;
	User user;

	@Data
	public static class User {
		String login_id;
	}
}
