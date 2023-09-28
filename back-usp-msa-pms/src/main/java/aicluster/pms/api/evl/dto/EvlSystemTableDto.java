package aicluster.pms.api.evl.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.entity.UsptEvlIemResult;
import lombok.Data;

@Data
public class EvlSystemTableDto implements Serializable {

	/**
	 * 평가시스템 평가항목
	 */
	private static final long serialVersionUID = 4237005555502665575L;
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
	/** 평가위원회ID */
	private String evlCmitId;

	List<UsptEvlIemResult> usptEvlIemResultList;

}
