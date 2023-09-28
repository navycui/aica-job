package aicluster.mvn.common.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MvnEtReqListItemDto implements Serializable {

	private static final long serialVersionUID = 3457450630754065086L;

	private String  mvnEtReqId	;	       /** 연장신청신청ID                   */
    @JsonIgnore
    private Date    mvnEtReqDt	;	       /** 연장신청신청일시                 */
    private String  mvnEtEndDay	;	       /** 연장신청종료일                   */
    private String  mvnEtReqSt	;	       /** 연장신청상태(G:MVN_ET_REQ_ST)    */
    private String  mvnEtReqStNm;		   /** 연장신청상태명 : CMMT_CODE.CODE_NM */
    @JsonIgnore
    private Date    mvnEtReqStDt;		   /** 연장신청상태변경일시             */
    private String  mvnId		;	       /** 입주ID                           */
    private String  mvnBeginDay	;	       /** 입주시작일                       */
    private String  mvnEndDay	;	       /** 입주종료일                       */
    private String  cmpnyId		;	       /** 업체ID : CMMT_MEMBER.MEMBER_ID */
    private String  cmpnyNm		;	       /** 업체명 : CMMT_MEMBER.MEMBER_NM */
    private String  chargerNm	;	       /** 업체담당명 : CMMT_MEMBER.CHARGER_NM */
    private String  ceoNm		;	       /** 대표자명                         */
    private String  mvnFcId		;	       /** 입주시설ID                       */
    private String  mvnFcNm		;	       /** 입주시설명                       */
    private String  bnoCd		;	       /** 건물동코드(G:BNO)                */
    private String  bnoNm		;	       /** 건물동코드명 : CMMT_CODE.CODE_NM */
    private String  roomNo		;	       /** 건물방호수                       */
    private String  mvnSt		;	       /** 입주상태(G:MVN_ST)               */
    private String  mvnStNm		;	       /** 입주상태명 : CMMT_CODE.CODE_NM */
    private String  mvnCmpnySt	;	       /** 입주업체상태(G:MVN_CMPNY_ST)     */
    private String  mvnCmpnyStNm;		   /** 입주업체상태명 : CMMT_CODE.CODE_NM */

    private long rn;

    public String getFmtMvnEtReqDt() {
        if (this.mvnEtReqDt == null) {
            return null;
        }
        return date.format(this.mvnEtReqDt, "yyyy-MM-dd");
    }

    public String getFmtMvnEtEndDay() {
        if (this.mvnEtEndDay == null || !date.isValidDate(this.mvnEtEndDay, "yyyyMMdd")){
            return null;
        }
        return date.format(string.toDate(this.mvnEtEndDay), "yyyy-MM-dd");
    }

    public String getFmtMvnEtReqStDt() {
        if (this.mvnEtReqStDt == null) {
            return null;
        }
        return date.format(this.mvnEtReqStDt, "yyyy-MM-dd HH:mm:ss");
    }

    public String getFmtMvnBeginDay() {
        if (this.mvnBeginDay == null || !date.isValidDate(this.mvnBeginDay, "yyyyMMdd")) {
            return null;
        }
        return date.format(string.toDate(this.mvnBeginDay), "yyyy-MM-dd");
    }

    public String getFmtMvnEndDay() {
        if (this.mvnEndDay == null || !date.isValidDate(this.mvnEndDay, "yyyyMMdd")){
            return null;
        }
        return date.format(string.toDate(this.mvnEndDay), "yyyy-MM-dd");
    }
}
