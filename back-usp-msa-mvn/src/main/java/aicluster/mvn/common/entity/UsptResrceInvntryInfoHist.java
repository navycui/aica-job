package aicluster.mvn.common.entity;

import java.io.Serializable;
import java.util.Date;

import bnet.library.util.CoreUtils.date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsptResrceInvntryInfoHist implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -8517984651438777497L;

	private String  histId        ;                 /** 이력ID : hist- */
    private Date    histDt        ;                 /** 이력일시 */
    private String  rsrcId        ;                 /** 자원ID */
    private String  rsrcGroupCd   ;                 /** 자원그룹코드(G:RSRC_GROUP) */
    private String  rsrcGroupNm   ;                 /** 자원그룹명 : CMMT_CODE.CODE_NM */
    private String  rsrcTypeNm    ;                 /** 자원유형명 */
    private String  rsrcTypeUnitCd;                 /** 자원유형단위코드(G:RSRC_TYPE_UNIT) */
    private String  rsrcTypeUnitNm;                 /** 자원유형단위명 : CMMT_CODE.CODE_NM */
    private Integer invtQy        ;                 /** 재고수량 */
    private Integer totalQy       ;                 /** 전체수량 */
    private Integer rsrcCalcQy    ;                 /** 자원연산량 */
    private String  workTypeNm    ;                 /** 작업구분명 */
    private String  workerId      ;                 /** 작업자ID : CMMV_USER.MEMBER_ID */
    private String  workerNm      ;                 /** 작업자명 : CMMV_USER.MEMBER_NM */

    public String getFmtHistDt() {
    	if (this.histDt == null) {
    		return null;
    	}
    	return date.format(this.histDt, "yyyy-MM-dd HH:mm:ss");
    }
}
