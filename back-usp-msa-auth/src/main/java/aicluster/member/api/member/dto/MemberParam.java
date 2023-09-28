package aicluster.member.api.member.dto;

//import bnet.library.util.CoreUtils.aes256;
//import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberParam {

	private String memberId;
	private String authorityId;
	private String memberSt;
	private String blackListBeginDay;
	private String blackListEndDay;
	private String blackListReason;
	private Boolean instr;

//	private String email;
//	private String mobileNo;
//
//	public String getEncEmail() {
//		if (string.isBlank(this.email)) {
//			return null;
//		}
//		return aes256.encrypt(this.email, this.memberId);
//	}
//
//	public String getEncMobileNo() {
//		if (string.isBlank(this.mobileNo)) {
//			return null;
//		}
//		return aes256.encrypt(this.mobileNo, this.memberId);
//	}
}
