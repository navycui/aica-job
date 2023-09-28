package aicluster.mvn.common.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlrsrcUserDto implements Serializable {
    /**
	 *
	 */
	private static final long serialVersionUID = -4194075267777974626L;

	private String  alrsrcId		;       /** 자원할당ID     */
	private String  evlLastSlctnId	;		/** 평가최종선정ID                */
    private String  lastSlctnTrgetId;       /** 최종선정대상ID                */
    private String  bsnsSlctnId		;       /** 사업선정대상ID */
    private String  receiptNo		;       /** 접수번호 : USPT_BSNS_PBLANC_APPLCNT.RECEIPT_NO */
    private boolean ordtmRcrit		;       /** 상시모집여부 : USPT_BSNS_PBLANC.ORDTM_RCRIT */
    private String  pblancNm		;       /** 공고명 : USPT_BSNS_PBLANC.PBLANC_NM */
    private String  alrsrcBgngDay	;	       /** 자원할당시작일 */
    private String  alrsrcEndDay	;	       /** 자원할당종료일 */
    private String  alrsrcSt		;       /** 자원할당상태(G:ALRSRC_ST) */
    private String  alrsrcStNm		;       /** 자원할당상태명 : CMMT_CODE.CODE_NM */

    private List<AlrsrcDstbUserDto>  alrsrcDstbUserList;  /** 자원할당배분(USPT_ALRSRC_DIST) Entity */

}
