package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class EvlSystemLogInDto implements Serializable {

	/**
	 * 평가시스템 login
	 */
	private static final long serialVersionUID = 1609496208197143968L;
	/** 평가위원회ID */
	private String evlCmitId;
	/** 평가위원회명 */
	private String  evlCmitNm;
}
