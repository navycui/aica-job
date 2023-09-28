package pki.sso;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SsoParam implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 5100194043205933112L;
	private String loginId;
    private String resultCode;
    private String key;

}
