package aicluster.framework.common.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CmmtAuthorRole implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -307492116598859122L;
	private String authorityId;		/** 권한ID */
	private String roleId;			/** 역할ID */
	private String creatorId;		/** 생성자ID */
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
