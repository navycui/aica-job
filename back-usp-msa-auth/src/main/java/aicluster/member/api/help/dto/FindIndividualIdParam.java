package aicluster.member.api.help.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindIndividualIdParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -7987516905239350441L;
	private String memberNm;
	private String birthday;
	private String mobileNo;
	private String email;
}
