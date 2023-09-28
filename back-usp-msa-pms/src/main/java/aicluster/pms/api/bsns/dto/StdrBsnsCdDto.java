package aicluster.pms.api.bsns.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class StdrBsnsCdDto implements Serializable {
	private static final long serialVersionUID = 593145789887859413L;
	/** 기준사업코드 */
	private String stdrBsnsCd;
}
