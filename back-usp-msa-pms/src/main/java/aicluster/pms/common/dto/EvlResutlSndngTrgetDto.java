package aicluster.pms.common.dto;

import java.io.Serializable;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvlResutlSndngTrgetDto implements Serializable {

	private static final long serialVersionUID = 3905633554633646193L;

	String  evlTrgetId; 	/*대상자id*/
	String  memberId; 		/*회원id*/
	String  memberNm; 		/*회원명*/
	String  encEmail;		/*이메일*/
	String  encMobileNo; 	/*핸드폰*/

	public String getEmail() {
		if (string.isBlank(this.encEmail)) {
			return null;
		}
		return aes256.decrypt(this.encEmail, this.memberId);
	}

	public String getMobileNo() {
		if (string.isBlank(this.encMobileNo)) {
			return null;
		}
		String mobileNo = aes256.decrypt(this.encMobileNo, this.memberId);
		return mobileNo;
	}
}
