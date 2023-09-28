package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsptEvlLastSlctn implements Serializable {
	private static final long serialVersionUID = 3493037621989596634L;
	/** 평가최종선정ID */
	private String evlLastSlctnId;
	/** 평가계획ID */
	private String evlPlanId;
	/** 최종선정여부 */
	private Boolean lastSlctn;
	/** 최종선정일시 */
	private Date lastSlctnDt;
	/** 선정통보여부 */
	private Boolean slctnDspth;
	/** 선정통보일시 */
	private Date slctnDspthDt;
	/** 선정발송방법 */
	private String slctnSndngMth;
	/** 선정발송내용 */
	private String slctnSndngCn;
	/** 탈락통보여부 */
	private Boolean ptrmsnDspth;
	/** 탈락통보일시 */
	private Date ptrmsnDspthDt;
	/** 탈락발송방법 */
	private String ptrmsnSndngMth;
	/** 탈락발송내용 */
	private String ptrmsnSndngCn;
	/** 최종선정처리구분코드(G:LAST_SLCTN_PROCESS_DIV) */
	private String lastSlctnProcessDivCd;
	/** 생성자ID */
	@JsonIgnore
	private String creatorId;
	/** 생성일시 */
	@JsonIgnore
	private Date createdDt;
	/** 수정자ID */
	@JsonIgnore
	private String updaterId;
	/** 수정일시 */
	@JsonIgnore
	private Date updatedDt;

	public Date getLastSlctnDt() {
		if (this.lastSlctnDt != null) {
			return new Date(this.lastSlctnDt.getTime());
         }
		return null;
	}
	public void setLastSlctnDt(Date lastSlctnDt) {
		this.lastSlctnDt = null;
		if (lastSlctnDt != null) {
			this.lastSlctnDt = new Date(lastSlctnDt.getTime());
		}
	}

	public Date getSlctnDspthDt() {
		if (this.slctnDspthDt != null) {
			return new Date(this.slctnDspthDt.getTime());
         }
		return null;
	}
	public void setSlctnDspthDt(Date slctnDspthDt) {
		this.slctnDspthDt = null;
		if (slctnDspthDt != null) {
			this.slctnDspthDt = new Date(slctnDspthDt.getTime());
		}
	}

	public Date getPtrmsnDspthDt() {
		if (this.ptrmsnDspthDt != null) {
			return new Date(this.ptrmsnDspthDt.getTime());
         }
		return null;
	}
	public void setPtrmsnDspthDt(Date ptrmsnDspthDt) {
		this.ptrmsnDspthDt = null;
		if (ptrmsnDspthDt != null) {
			this.ptrmsnDspthDt = new Date(ptrmsnDspthDt.getTime());
		}
	}

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
