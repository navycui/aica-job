package aicluster.member.common.util.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionEmailCertDto implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 5329608489565188945L;
	private String certNo;
	private String email;
	private boolean checked;
}
