package aicluster.member.api.help.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginIdDto implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -8583473829620167979L;
	private String loginId;
}
