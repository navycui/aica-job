package aicluster.member.api.help.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BzmnPasswdParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -4117807968743611670L;
	private String loginId;
	private String memberNm;
	private String bizrno;
	private String email;
	private String mobileNo;
}
