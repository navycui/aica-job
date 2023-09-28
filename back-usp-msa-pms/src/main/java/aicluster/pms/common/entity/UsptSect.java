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
public class UsptSect implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -8118328546218449041L;

	/** 변경 플래그(I,U,D) */
	String  flag;
	/** 분과ID */
	String  sectId;
	/** 평가계획ID */
	String  evlPlanId;
	/** 분과명 */
	String  sectNm;
	/** 평가대상수 */
	Integer evlTrgetCo;

	/** 생성자 ID */
	@JsonIgnore
	private String creatorId;
	/** 생성일시 */
	@JsonIgnore
	private Date createdDt;
	/** 수정자 ID */
	@JsonIgnore
	private String updaterId;
	/** 수정일시 */
	@JsonIgnore
	private Date updatedDt;

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
