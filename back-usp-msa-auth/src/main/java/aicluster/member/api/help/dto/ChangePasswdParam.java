package aicluster.member.api.help.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswdParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 7588027647475430107L;
	private String key;
	private String passwd1;
	private String passwd2;
}
