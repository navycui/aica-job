package aicluster.member.api.help.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindBzmnIdParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 6368539102076711564L;
	private String memberNm;
	private String bizrno;
	private String mobileNo;
	private String email;
}
