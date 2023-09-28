package aicluster.tsp.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EqpmnRegisterAllList implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 6848728032042999538L;
	private String eqpmnId;						/** 장비ID */
	private String eqpmnRentalSt;				/** 장비대여신청상태(G:EQPMN_RENTAL_ST) */
	private String beginDay;					/** 시작일 */
	private String endDay;						/** 종료일 */
	private String paymentMethod;				/** 지불방법(G:PAYMENT_METHOD) */
	private String receiptNo;					/** 접수번호(ER+8자리순번) */
	private Date createdDt;						/** 생성일시 */
	private String mberTyCode;					/** 회원구분(G:MEMBER_TYPE) */
	private String mberNm;					/** 회원명(사업자명) */
	private String assetNo;						/** 자산번호 */
	private String eqpmnNmKo;					/** 장비명(국문) */


	private String rn;							/** 번호 */

	public Date getCreatedDt() {
		if (this.createdDt != null) {
			return new Date(this.createdDt.getTime());
		}
		return null;
	}
	public void setCreatedDt(Date createdDt) {
		this.createdDt = null;
		if (createdDt != null) {
			this.createdDt = new Date(createdDt.getTime());
		}
	}
}