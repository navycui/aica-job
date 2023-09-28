package aicluster.batch.common.entity;

import java.io.Serializable;
import java.util.Date;

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
    private Date    checkoutReqDt  ;                /** 퇴실신청일시 */
    private String  checkoutPlanDay;                /** 퇴실예정일 */
    private String  checkoutReqSt  ;                /** 퇴실신청상태(G:CHECKOUT_REQ_ST) */
    private Date    checkoutReqStDt;                /** 퇴실신청상태변경일시 */
    private boolean mvnCheckoutYn  ;                /** 입주업체퇴실여부 */
    private String  updaterId      ;                /** 수정자ID(CMMT_MEMBER.MEMBER_ID) */
    private Date    updatedDt      ;                /** 수정일시 */
    
    public Date getCheckoutReqDt() {
		if (this.checkoutReqDt != null) {
			return new Date(this.checkoutReqDt.getTime());
		}
		return null;
	}
	
	public void setCheckoutReqDt(Date checkoutReqDt) {
		this.checkoutReqDt = null;
		if (checkoutReqDt != null) {
			this.checkoutReqDt = new Date(checkoutReqDt.getTime());
		}
	}
	
	public Date getCheckoutReqStDt() {
		if (this.checkoutReqStDt != null) {
			return new Date(this.checkoutReqStDt.getTime());
		}
		return null;
	}
	
	public void setCheckoutReqStDt(Date checkoutReqStDt) {
		this.checkoutReqStDt = null;
		if (checkoutReqStDt != null) {
			this.checkoutReqStDt = new Date(checkoutReqStDt.getTime());
		}
	}
	
	public Date getUpdatedDt() {
		if (this.updatedDt != null) {
			return new Date(this.updatedDt.getTime());
		}
		return null;
	}
	
	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = null;
		if (updatedDt != null) {
			this.updatedDt = new Date(updatedDt.getTime());
		}
	}
}
