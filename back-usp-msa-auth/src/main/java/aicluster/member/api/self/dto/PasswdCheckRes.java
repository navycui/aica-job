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
public class PasswdCheckRes implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 9142211405775905221L;
	private String passwdCheckKey;
}
