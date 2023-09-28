package aicluster.member.api.insider.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswdParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 7917742173209160511L;
	private String oldPasswd;
	private String newPasswd1;
	private String newPasswd2;
}
