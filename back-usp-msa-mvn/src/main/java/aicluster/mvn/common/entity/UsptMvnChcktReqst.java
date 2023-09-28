package aicluster.mvn.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsptMvnChcktReqst implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 3203838769564277143L;

	private String  checkoutReqId  ;                /** 퇴실신청ID */
    private String  mvnId          ;                /** 입주ID */
    @JsonIgnore
    private Date    checkoutReqDt  ;                /** 퇴실신청일시 */
    private String  checkoutPlanDay;                /** 퇴실예정일 */
    private String  checkoutReason ;                /** 퇴실사유 */
    private String  checkoutReqSt  ;                /** 퇴실신청상태(G:CHECKOUT_REQ_ST) */
    private String  checkoutReqStNm;                /** 퇴실신청상태명 : CMMT_CODE.CODE_NM */
    @JsonIgnore
    private Date    checkoutReqStDt;                /** 퇴실신청상태변경일시 */
    private String  makeupReqCn    ;                /** 보완요청내용 */
    private String  equipRtdtl     ;                /** 장비반납내역 */
    private boolean mvnCheckoutYn  ;                /** 입주업체퇴실여부 */
    @JsonIgnore
    private String  creatorId      ;                /** 생성자ID(CMMT_MEMBER.MEMBER_ID) */
    @JsonIgnore
    private Date    createdDt      ;                /** 생성일시 */
    @JsonIgnore
    private String  updaterId      ;                /** 수정자ID(CMMT_MEMBER.MEMBER_ID) */
    @JsonIgnore
    private Date    updatedDt      ;                /** 수정일시 */

    public String getFmtCheckoutReqDt() {
        if (this.checkoutReqDt == null) {
            return null;
        }
        return date.format(this.checkoutReqDt, "yyyy-MM-dd");
    }

    public String getFmtCheckoutPlanDay() {
        if (this.checkoutPlanDay == null || date.isValidDate(this.checkoutPlanDay, "yyyyMMdd")) {
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
}
