package aicluster.member.api.help.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswdDto implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 5585342309296020069L;
	private String key;
	private String mobileNo;
	private String email;
}
