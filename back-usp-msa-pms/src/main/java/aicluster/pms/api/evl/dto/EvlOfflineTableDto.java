package aicluster.pms.api.evl.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.entity.UsptEvlIemResult;
import lombok.Data;

@Data
public class EvlOfflineTableDto implements Serializable {

	/**
	 *  오프라인 평가항목
	 */
	private static final long serialVersionUID = -7919447611973262410L;
	/** 평가결과ID */
	private String  evlResultId;
	/** 평가위원ID */
	private String evlMfcmmId;
	/**평가대상ID*/
	private String evlTrgetId;
	/** 평가의견 */
	private String evlOpinion;
	/** 평가방식코드(G:EVL_METHOD) */
	private String evlMthdCd;

	List<UsptEvlIemResult> usptEvlIemResultList;

}
