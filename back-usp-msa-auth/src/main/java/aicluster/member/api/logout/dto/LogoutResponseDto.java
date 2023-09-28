package aicluster.member.api.logout.dto;

import java.io.Serializable;

import aicluster.member.api.member.dto.MemberParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogoutResponseDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6147774722124656365L;

	private String result;
	private int resultCode;
}
