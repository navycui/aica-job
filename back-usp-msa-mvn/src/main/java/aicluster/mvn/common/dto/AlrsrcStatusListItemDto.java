package aicluster.mvn.common.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlrsrcStatusListItemDto implements Serializable{

    /**
	 *
	 */
	private static final long serialVersionUID = 2612069678405521492L;

	private String  alrsrcId		;       /** 자원할당ID                    */
	private String  evlLastSlctnId	;		/** 평가최종선정ID                */
    private String  lastSlctnTrgetId;       /** 최종선정대상ID                */
    private String  bsnsSlctnId		;       /** 사업선정대상ID                */
    private String  receiptNo		;       /** 접수번호 : USPT_BSNS_PBLANC_APPLCNT.RECEIPT_NO */
    private String  cmpnyId			;       /** 업체ID                        */
    private String  cmpnyNm			;       /** 업체명(CMMT_MEMBER.MEMBER_NM) */
    private boolean ordtmRcrit		;       /** 상시모집여부 : USPT_BSNS_PBLANC.ORDTM_RCRIT */
    private String  pblancNm		;       /** 공고명 : USPT_BSNS_PBLANC.PBLANC_NM */
    private String  alrsrcBgngDay	;	       /** 자원할당시작일                */
    private String  alrsrcEndDay	;	       /** 자원할당종료일                */
    private String  alrsrcSt		;       /** 자원할당상태(G:ALRSRC_ST)     */
    private String  alrsrcStNm		;       /** 자원할당상태명 : CMMT_CODE.CODE_NM */

    private long rn;

    private List<AlrsrcDstbStatusDto>  alrsrcDstbSttusList;  /** USPT_ALRSRC_DSTB, USPT_ALRSRC_FNINF 조합 목록 */

}
