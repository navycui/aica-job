package aicluster.member.api.login.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KakaoTokenResponse {
	private String error;
	private String error_description;
	private String error_code;

	private String access_token;
	private String token_type;
	private String refresh_token;
	private Long expires_in;
	private Long refresh_token_expires_in;
}
