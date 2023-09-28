package aicluster.member.api.login.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NaverLoginResponse {
	private String resultcode;
	private String message;
	private NaverLoginUser response;
}
