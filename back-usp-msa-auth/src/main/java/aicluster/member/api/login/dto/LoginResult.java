package aicluster.member.api.login.dto;

import aicluster.framework.common.entity.TokenDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResult extends TokenDto {

	private Boolean changePasswd;	/** 비밀번호 변경 시점여부 */
	private Boolean initPasswd;		/** 초기 비밀번호 여부 */
	private String dbpw; 			/** DB에 저장된 hash 비밀번호 */
	private String dbid;

}
