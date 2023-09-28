package aicluster.member.api.self.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BzmnPhoneCertParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -1322027016787483073L;
	private String mobileNo;
	private String sessionId;
}
