package aicluster.mvn.common.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlrsrcDstbStatusDto implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = -5487817492956039697L;

	private String  alrsrcId	;	       /** 자원할당ID                 */
    private String  rsrcId		;	       /** 자원ID                     */
    private String  rsrcGroupCd	;	       /** 자원그룹코드(G:RSRC_GROUP) */
    private String  rsrcGroupNm	;	       /** 자원그룹명                 */
    private String  rsrcTypeNm	;	       /** 자원유형명                 */
    private boolean rsrcUseYn   ;          /** 자원사용여부               */
    private long    rsrcDstbQy	;	       /** 자원배분량                 */
    private long    totalCalcQy ;		   /** 전체연산량 : USPT_ALRSRC_DSTB.RSRC_DSTB_TY * USPT_ALRSRC_FNINF.RSRC_CALC_QY */

}
