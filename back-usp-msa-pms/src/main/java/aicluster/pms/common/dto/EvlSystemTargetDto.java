package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class EvlSystemTargetDto implements Serializable {

	/**
	 * 평가시스템 평가대상 Dto
	 */
	private static final long serialVersionUID = -5533742185490325405L;
	/** 평가위원회ID */
	private String evlCmitId;
	/**평가대상ID*/
	private String evlTrgetId;
	/** 접수번호 */
	private String receiptNo;
	/** 회원명 */
	private String memberNm;
	/** 총점 */
	private Integer sumEvlScore;
	/** 평가방식코드(G:EVL_METHOD) */
	private String evlMthdCd;
	 /** 위원평가상태코드(G:MFCMM_EVL_STTUS) */
	private String  mfcmmEvlSttusCd;
	/** 위원평가상태코드명*/
	private String  mfcmmEvlSttusNm;
	/** 평가위원ID */
	private String evlMfcmmId;
	/** 첨부파일그룹ID */
	private String  attachmentGroupId;
}
