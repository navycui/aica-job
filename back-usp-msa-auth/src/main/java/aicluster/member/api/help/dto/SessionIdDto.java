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
public class SessionIdDto implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1341705301097244347L;
	private String key;
}
