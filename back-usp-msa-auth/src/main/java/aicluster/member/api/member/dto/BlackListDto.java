package aicluster.member.api.member.dto;

import java.io.Serializable;
import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlackListDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -6753392276518699840L;
	private String memberId;
	private String[] registerReasons;
	private String limitBeginDt;
	private String limitEndDt;
	private String detailReason;

	public String[] getRegisterReasons() {
		if(this.registerReasons != null) {
			return Arrays.copyOf(this.registerReasons, this.registerReasons.length);
		}
		return null;
	}

	public void setRegisterReasons(String[] registerReasons) {
		this.registerReasons = null;
		if(registerReasons != null) {
			this.registerReasons = Arrays.copyOf(registerReasons, registerReasons.length);
		}
	}

}
