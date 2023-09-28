package aicluster.mvn.common.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import aicluster.mvn.common.entity.UsptMvnEntrpsInfo;
import aicluster.mvn.common.entity.UsptMvnFcltyInfo;
import bnet.library.util.CoreUtils.date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutReqDto implements Serializable {

	private static final long serialVersionUID = 4303394761985156543L;

	private String  checkoutReqId	;	       /** 퇴실신청ID                        */
    private String  mvnId			;       /** 입주ID                            */
    @JsonIgnore
    private Date    checkoutReqDt 	;	       /** 퇴실신청일시                      */
    private String  checkoutPlanDay	;	       /** 퇴실예정일                        */
    private String  checkoutReason	;	       /** 퇴실사유                          */
    private String  checkoutReqSt	;	       /** 퇴실신청상태(G:CHECKOUT_REQ_ST)  */
    private String  checkoutReqStNm	;	       /** 퇴실신청상태명 : CMMT_CODE.CODE_NM */
    @JsonIgnore
    private Date    checkoutReqStDt	;	       /** 퇴실신청상태변경일시              */
    private String  makeupReqCn		;       /** 보완요청내용                      */
    private String  equipRtdtl		;       /** 장비반납내역                      */

    private UsptMvnEntrpsInfo mvnCompany;	       /** USPT_MVN_COMPANY Entity           */
    private UsptMvnFcltyInfo      mvnFc	;	       /** USPT_MVN_FC Entity                */
    private MvnUserDto     mvnUser  ;          /** 입주자 정보 */

    public String getFmtCheckoutReqDt() {
        if (this.checkoutReqDt == null) {
            return null;
        }
        return date.format(this.checkoutReqDt, "yyyy-MM-dd");
    }

    public String getFmtCheckoutReqStDt() {
        if (this.checkoutReqStDt == null) {
            return null;
        }
        return date.format(this.checkoutReqStDt, "yyyy-MM-dd HH:mm:ss");
    }
}
