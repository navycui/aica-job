package aicluster.pms.common.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class EvlSttusMfcmmListDto implements Serializable {
	private static final long serialVersionUID = -2926220319661740918L;
	/** 평가위원ID */
	private String evlMfcmmId;
	/** 평가위원명 */
	private String evlMfcmmNm;
	/** 평가대상ID */
	@JsonIgnore
	private String evlTrgetId;
	/** 위원평가상태코드 (G: MFCMM_EVL_STTUS) */
	private String mfcmmEvlSttusCd;
	/** 위원평가상태 */
	private String mfcmmEvlSttusNm;
	/** 평가점수 */
	private Integer evlScore;
}
