package aicluster.member.api.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityRequestDto {

	private String authorityId;
	private String authorityNm;
	private String[] roleId;
}
