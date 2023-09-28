package aicluster.mvn.common.dto;

import java.io.Serializable;

import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CmpnyPrfrmListItemDto implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = -8163378939915826709L;

	private String  mvnId		;	       /** 입주ID                        */
    private String  cmpnyId		;	       /** 업체ID                        */
    private String  cmpnyNm		;	       /** 업체명 : CMMT_MEMBER.MEMBER_NM */
    private String  bnoCd		;	       /** 건물동코드(G:BNO) */
    private String  bnoNm		;	       /** 건물동명칭 : CMMT_CODE.CODE_NM */
    private String  roomNo		;	       /** 건물방호수                    */
    private String  mvnCmpnySt	;	       /** 입주업체상태코드 */
    private String  mvnCmpnyStNm;          /** 입주업체상태명 */
    private String  sbmsnYm		;	       /** 제출년월                      */
    private String  sbmsnDay	;	       /** 제출일자                      */
    private String  rsltSttusCd	;	       /** 성과상태코드(G:RSLT_STTUS) */
    private String  rsltSttusNm	;	       /** 성과상태명 : CMMT_CODE.CODE_NM */

    private long rn;

    public String getFmtSbmsnYm() {
    	if (string.isBlank(this.sbmsnYm) || date.isValidDate(this.sbmsnYm, "yyyyMM")) {
    		return null;
    	}
    	return date.format(string.toDate(this.sbmsnYm), "yyyy-MM");
    }

    public String getFmtSbmsnDay() {
    	if (string.isBlank(this.sbmsnDay) || !date.isValidDate(this.sbmsnDay, "yyyyMMdd")) {
    		return null;
    	}
    	return date.format(string.toDate(this.sbmsnDay), "yyyy-MM-dd");
	}
}
