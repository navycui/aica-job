package aicluster.member.api.login.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleMeParam {
	private String personFields;
	private String key;
	private String access_token;
}
