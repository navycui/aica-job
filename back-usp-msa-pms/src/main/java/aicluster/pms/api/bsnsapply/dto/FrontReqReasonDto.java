package aicluster.pms.api.bsnsapply.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class FrontReqReasonDto implements Serializable {
	private static final long serialVersionUID = -4039712405450655206L;
	/** 요청일시 */
	private String reqDt;
	/** 사유 */
	private String reason;
}
