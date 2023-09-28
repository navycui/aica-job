package aicluster.mvn.common.dto;

import java.io.Serializable;

import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MvnCmpnyListItemDto implements Serializable {

	private static final long serialVersionUID = 6592412979249906482L;

	private String  mvnId			;      /** 입주ID                           */
    private String  cmpnyId			;      /** 업체ID : CMMT_MEMBER.MEMBER_ID */
    private String  cmpnyNm			;      /** 업체명 : CMMT_MEMBER.MEMBER_NM */
    private String  bnoCd			;      /** 건물동코드(G:BNO)                */
    private String  bnoNm			;      /** 건물동코드명 : CMMT_CODE.CODE_NM */
    private String  roomNo			;      /** 건물방호수                       */
    private String  mvnBeginDay		;      /** 입주시작일                       */
    private String  mvnEndDay		;      /** 입주종료일                       */
    private String  mvnCmpnySt		;      /** 입주업체상태(G:MVN_CMPNY_ST)     */
    private String  mvnCmpnyStNm	;	       /** 입주업체상태명 : CMMT_CODE.CODE_NM */
    private String  mvnAllocateSt	;	       /** 입주배정상태(G:MVN_ALLOCATE_ST)     */
    private String  mvnAllocateStNm	;	       /** 입주배정상태명 : CMMT_CODE.CODE_NM */

    private long rn;

    public String getFmtMvnBeginDay() {
        if (string.isBlank(this.mvnBeginDay) || !date.isValidDate(this.mvnBeginDay, "yyyyMMdd")) {
            return null;
        }
        return date.format(string.toDate(this.mvnBeginDay), "yyyy-MM-dd");
    }

    public String getFmtMvnEndDay() {
        if (string.isBlank(this.mvnEndDay) || !date.isValidDate(this.mvnEndDay, "yyyyMMdd")) {
            return null;
        }
        return date.format(string.toDate(this.mvnEndDay), "yyyy-MM-dd");
    }
}
