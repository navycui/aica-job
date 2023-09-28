package aicluster.pms.api.evl.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class EvlTrgetIdParam implements Serializable {
	private static final long serialVersionUID = -1490713056604064435L;
	/** 평가대상ID */
	private String evlTrgetId;
}
