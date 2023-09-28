package pki.sso;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SsoCheckResult implements Serializable {
	private static final long serialVersionUID = -5053995872398066001L;
	String command;
	String resultCode;
	String resultMessage;
}
