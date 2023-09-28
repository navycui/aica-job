package aicluster.member.api.login.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KakaoLoginResponse {
	private Integer code;
	private String msg;
	private String id;
	private String connected_dt;
}
