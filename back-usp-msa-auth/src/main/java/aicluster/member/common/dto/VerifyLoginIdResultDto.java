package aicluster.member.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class VerifyLoginIdResultDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4626971900059668583L;

	private boolean duplicateYn;
	private String loginId;
}
