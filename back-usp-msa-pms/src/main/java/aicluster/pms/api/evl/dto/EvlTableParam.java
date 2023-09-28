package aicluster.pms.api.evl.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class EvlTableParam implements Serializable {
	private static final long serialVersionUID = -7161875708902773187L;
	/** 평가위원회ID */
	private String evlCmitId;
	/** 평가대상ID */
	private String evlTrgetId;
	/** 평가위원ID */
	private String evlMfcmmId;
}
