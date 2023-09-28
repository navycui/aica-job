package aicluster.mvn.common.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlrsrcDstbUserDto implements Serializable {
    /**
	 *
	 */
	private static final long serialVersionUID = -6436988607736718011L;

	private String  alrsrcId	;	       /** 자원할당ID    */
    private String  rsrcId		;	       /** 자원ID        */
    private String  rsrcGroupCd	;	       /** 자원그룹코드(G:RSRC_GROUP) */
    private String  rsrcGroupNm	;	       /** 자원그룹명 : CMMT_CODE.CODE_NM */
    private String  rsrcTypeNm	;	       /** 자원유형명 */
    private String  rsrcTypeUnitCd;        /** 자원유형단위코드 */
    private String  rsrcTypeUnitNm;        /** 자원유형단위명   */
    private boolean rsrcUseYn	;	       /** 자원사용여부  */
    private Long    rsrcDstbQy	;	       /** 자원배분량    */
    private String  rsrcDstbCn	;	       /** 자원배분내용  */

}
