package aicluster.mvn.common.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlrsrcFninfStatusDto implements Serializable{

    /**
	 *
	 */
	private static final long serialVersionUID = 6345795654236319731L;

	private String  rsrcId			;       /** 자원ID                              */
    private String  rsrcGroupCd		;       /** 자원그룹코드(G:RSRC_GROUP)          */
    private String  rsrcGroupNm		;       /** 자원그룹명 : CMMT_CODE.CODE_NM */
    private String  rsrcTypeNm		;       /** 자원유형명                          */
    private String  rsrcTypeUnitCd	;	    /** 자원유형단위코드(G:RSRC_TYPE_UNIT)  */
    private String  rsrcTypeUnitNm	;	    /** 자원유형단위명 : CMMT_CODE.CODE_NM */
    private Long    invtQy			;       /** 재고수량                            */
    private Long    dstbQy			;       /** 배분수량                            */
    private Long    totalQy			;       /** 전체수량                            */
    private Long    totalCalcQy		;	    /** 전체연산량 : SUM(USPT_ALRSRC_DSTB.RSRC_DSTB_QY) * USPT_ALRSRC_FNINF.RSRC_CALC_QY */

}
