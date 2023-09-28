package aicluster.member.api.help.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneCertReqParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -2346083539330897093L;
	private String key;
	private String mobileNo;
}
