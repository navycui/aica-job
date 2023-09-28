package aicluster.pms.api.bsns.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class BsnsCdDto implements Serializable {
	private static final long serialVersionUID = -9125379906956561369L;
	/** 사업코드 */
	private String BsnsCd;
}
