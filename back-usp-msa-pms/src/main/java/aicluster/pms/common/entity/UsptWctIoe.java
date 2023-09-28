package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UsptWctIoe implements Serializable {
	private static final long serialVersionUID = 2862711509973219536L;
	private String wctIoeId;		/** 사업비비목ID */
	private String wctIoeNm;		/** 사업비비목명 */
	private String flag;			/** I:등록, U:수정, D:삭제 */
	@JsonIgnore
	private String creatorId;		/** 생성자ID */
	@JsonIgnore
	private Date createdDt;			/** 생성일시 */
	@JsonIgnore
	private String updaterId;		/** 수정자ID */
	@JsonIgnore
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
