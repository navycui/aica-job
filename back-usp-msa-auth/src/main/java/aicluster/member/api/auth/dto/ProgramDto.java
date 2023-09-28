package aicluster.member.api.auth.dto;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramDto {

	private String programId;
	private String programNm;
	private String systemId;
	private String httpMethod;
	private String urlPattern;
	private Long checkOrder;
	private String[] roles;

 	public String[] getRoles() {
 		if(this.roles != null) {
 			return Arrays.copyOf(this.roles, this.roles.length);
 		}
 		return null;
	}

	public void setRoles(String[] roles) {
		this.roles = null;
		if(roles != null) {
			this.roles = Arrays.copyOf(roles, roles.length);
		}
	}

}
