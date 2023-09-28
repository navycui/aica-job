package aicluster.member.api.help.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndividualPasswdParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 3928392656410142966L;
	private String loginId;
	private String memberNm;
	private String birthday;
	private String email;
	private String mobileNo;
}
