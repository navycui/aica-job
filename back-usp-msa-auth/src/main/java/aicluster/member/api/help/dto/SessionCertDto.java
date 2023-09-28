package aicluster.member.api.help.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionCertDto implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 8425896796440451682L;
	private String certNo;
	private String memberId;
}
