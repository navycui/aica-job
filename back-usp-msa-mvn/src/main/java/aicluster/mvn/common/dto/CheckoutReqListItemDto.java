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
public class CheckoutReqListItemDto implements Serializable {

	private static final long serialVersionUID = -2464799458511226120L;

	private String  checkoutReqId	;	       /** 퇴실신청ID                       */
    private Date    checkoutReqDt 	;	       /** 퇴실신청일시                     */
    private String  checkoutPlanDay	;	       /** 퇴실예정일                       */
    private String  checkoutReqSt	;	       /** 퇴실신청상태(G:CHECKOUT_REQ_ST)  */
    private String  checkoutReqStNm	;	       /** 퇴실신청상태명                   */
    @JsonIgnore
    private Date    checkoutReqStDt	;	       /** 퇴실신청상태변경일시             */
    private String  mvnId			;          /** 입주ID                           */
    private String  mvnBeginDay		;          /** 입주시작일                       */
    private String  mvnEndDay		;          /** 입주종료일                       */
    private String  cmpnyId			;          /** 업체ID : CMMT_MEMBER.MEMBER_ID */
    private String  cmpnyNm			;          /** 업체명 : CMMT_MEMBER.MEMBER_NM */
    private String  chargerNm		;          /** 업체담당명 : CMMT_MEMBER.CHARGER_NM */
    private String  ceoNm			;          /** 대표자명                         */
    private String  mvnFcId			;          /** 입주시설ID                       */
    private String  mvnFcNm			;          /** 입주시설명                       */
    private String  bnoCd			;          /** 건물동코드(G:BNO)                */
    private String  bnoNm			;          /** 건물동코드명 : CMMT_CODE.CODE_NM */
    private String  roomNo			;          /** 건물방호수                       */
    private String  mvnSt			;          /** 입주상태(G:MVN_ST)               */
    private String  mvnStNm			;          /** 입주상태명 : CMMT_CODE.CODE_NM */
    private String  mvnCmpnySt		;          /** 입주업체상태(G:MVN_CMPNY_ST)     */
    private String  mvnCmpnyStNm	;	       /** 입주업체상태명 : CMMT_CODE.CODE_NM */

    private long rn;

    public String getFmtCheckoutReqDt() {
        if (this.checkoutReqDt == null) {
            return null;
        }
        return date.format(this.checkoutReqDt, "yyyy-MM-dd");
    }

    public String getFmtCheckoutPlanDay() {
        if (this.checkoutPlanDay == null || !date.isValidDate(this.checkoutPlanDay, "yyyyMMdd")){
            return null;
        }
        return date.format(string.toDate(this.checkoutPlanDay), "yyyy-MM-dd");
    }

    public String getFmtCheckoutReqStDt() {
        if (this.checkoutReqStDt == null) {
            return null;
        }
        return date.format(this.checkoutReqStDt, "yyyy-MM-dd HH:mm:ss");
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
