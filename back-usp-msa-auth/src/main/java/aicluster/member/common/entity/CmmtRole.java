package aicluster.member.common.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CmmtRole implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 4960586204873828741L;
	private String roleId;			/** 역할ID */
	private String roleNm;			/** 역할 이름 */
	private String creatorId;		/** 생성자ID */
	private Date createdDt;			/** 생성일시 */
	private String updaterId;		/** 수정자ID */
	private Date updatedDt;			/** 수정일시 */

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
