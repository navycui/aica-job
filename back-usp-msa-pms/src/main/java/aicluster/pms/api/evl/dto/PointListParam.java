package aicluster.pms.api.evl.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class PointListParam implements Serializable {
	private static final long serialVersionUID = 1776971472318752084L;
	/** 평가위원회ID */
	private String evlCmitId;
	/** 평가대상ID */
	private String evlTrgetId;
	/** 평가구분코드 */
	private String evlDivCd;
}
