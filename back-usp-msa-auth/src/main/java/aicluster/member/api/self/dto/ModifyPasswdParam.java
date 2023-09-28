package aicluster.member.api.self.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyPasswdParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 5092896750068737189L;
	private String oldPasswd;
	private String newPasswd1;
	private String newPasswd2;
}
