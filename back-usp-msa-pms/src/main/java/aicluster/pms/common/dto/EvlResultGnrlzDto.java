package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class EvlResultGnrlzDto implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -4409816937482885693L;

	String  evlMfcmmId;                     /** 평가위원ID */
	String  evlMfcmmNm;                     /** 평가위원명 */
	String  evlOpinion;         			/** 평가의견 */
	String  evlCn;              			/** 평가내용 */
	String  evlIemId;           			/** 평가항목ID */
	String  evlTrgetId;                     /** 평가대상ID */
	String  evlCmitId;                      /** 평가위원회ID */
}