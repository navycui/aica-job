package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class BsnsNmDto implements Serializable {
	private static final long serialVersionUID = -2951854262228764004L;
	/** 사업코드 */
	private String bsnsCd;
	/** 사업명 */
	private String bsnsNm;
}
