package aicluster.member.api.member.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -8243955396233383856L;

	private String loginId;
	private String passwd1;
	private String passwd2;
	private String memberNm;
	private String memberType;
	private String email;
	private String mobileNo;
	private String chargerNm;
	private String ceoNm;
	private String jurirno;
	private Boolean marketingReception;
	private String sessionId;

}
