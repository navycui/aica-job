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
public class LoginResult implements Serializable {
	private static final long serialVersionUID = 9104163045130879543L;
	String accessToken;
	Long accessTokenExpiresIn;
	Boolean changePasswd;
	String grantType;
	Boolean initPasswd;
	String refreshToken;
	Long refreshTokenExpiresIn;
}
