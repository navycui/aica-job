package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UsptBsnsSlctn implements Serializable {
	private static final long serialVersionUID = 8525857290129699846L;
	/** 사업선정대상ID */
	private String  bsnsSlctnId;
	/** 최종선정대상ID */
	private String lastSlctnTrgetId;
	/** 총사업비 */
	private Long totalWct;
	/** 지원예산 */
	private Long sportBudget;
	/** 부담금현금 */
	private Long alotmCash;
	/** 부담금현물 */
	private Long alotmActhng;
	/** 부담금소계 (현금+현물) */
	private Long alotmSum;
	/** 생성자 ID */
	@JsonIgnore
	private String creatorId;
	/** 생성일시 */
	@JsonIgnore
	private Date createdDt;

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
