package aicluster.mvn.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class AlrsrcCmpnyListItemDto implements Serializable {

	private static final long serialVersionUID = -4471997883714205356L;

	private String  alrsrcId		;       /** 자원할당ID                    */
	private String  evlLastSlctnId	;		/** 평가최정선정ID                */
    private String  lastSlctnTrgetId;       /** 최종선정대상ID                */
    private String  bsnsSlctnId		;       /** 사업선정대상ID                */
    private String  receiptNo		;       /** 접수번호 : USPT_BSNS_PBLANC_APPLCNT.RECEIPT_NO */
    private String  cmpnyId			;       /** 업체ID                        */
    private String  cmpnyNm			;       /** 업체명(CMMT_MEMBER.MEMBER_NM) */
    private boolean ordtmRcrit		;       /** 상시모집여부 : USPT_BSNS_PBLANC.ORDTM_RCRIT */
    private String  pblancNm		;       /** 공고명 : USPT_BSNS_PBLANC.PBLANC_NM */
    private String  alrsrcBgngDay	;	    /** 자원할당시작일                */
    private String  alrsrcEndDay	;	    /** 자원할당종료일                */
    private String  alrsrcSt		;       /** 자원할당상태(G:ALRSRC_ST)     */
    private String  alrsrcStNm		;       /** 자원할당상태명 : CMMT_CODE.CODE_NM */
    private String  gpuAlrsrcCn		;       /** 가속기 자원할당내용           */
    private String  strgAlrsrcCn	;	    /** 소토리지 자원할당내용         */
    private String  saasAlrsrcCn	;	    /** Saas 자원할당내용             */
    private String  dtlkAlrsrcCn	;	    /** 데이터레이크 자원할당내용     */

    private long rn;
}
