package aicluster.member.api.self.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BzmnEmailCertParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -4630870794826595381L;
	private String email;
	private String sessionId;
}
