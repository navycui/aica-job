package aicluster.member.api.login.dto;

import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleLoginResponse {
	private Integer code;
	private String message;
	private String status;
	private String resourceName;

	public String getPersonId() {
		if (string.isBlank(resourceName)) {
			return null;
		}
		String[] tokens = string.split(resourceName, '/');
		if (tokens.length > 1) {
			return tokens[1];
		}
		return null;
	}
}
