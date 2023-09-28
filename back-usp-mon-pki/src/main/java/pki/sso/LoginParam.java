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
public class LoginParam implements Serializable {
	private static final long serialVersionUID = 9106614341925355192L;
	Boolean errorId;
	Boolean errorPw;
	Boolean isLock;
	Boolean open;
	String labelId;
	String labelPw;
	String loginId;
	String passwd;
}
