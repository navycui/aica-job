package aicluster.member.api.login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EvlLoginParam {

	private String evlCmitId;
    private String expertNm;
    private String mobileNo;
    private String email;
    private String passwd;

}
