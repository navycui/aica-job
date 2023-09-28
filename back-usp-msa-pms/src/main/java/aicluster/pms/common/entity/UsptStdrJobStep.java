package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UsptStdrJobStep implements Serializable {

	private static final long serialVersionUID = 449091251285546085L;

	private String stdrBsnsCd;		/** 기준사업코드 */
	private String jobStepCd;		/** 업무단계 */

	@JsonIgnore
	private String creatorId;		/** 생성자ID */
	@JsonIgnore
	private Date createdDt;			/** 생성일시 */

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
