package aicluster.member.api.login.dto;

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
	private static final long serialVersionUID = 5896387254889115584L;
	private String loginId;
    private String resultCode;
    private String key;

}
