package aicluster.framework.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.json;
import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmmtSesionInfo implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -9037586113294229834L;
	private String sessionId;
	private String sessionValue;
	private Date expiredDt;
	private Date createdDt;

	@JsonIgnore
	public <T> T getSessionJsonValue(Class<T> cls) {
		if (string.isBlank(sessionValue)) {
			return null;
		}

		return json.toObject(sessionValue, cls);
	}
	
	public Date getExpiredDt() {
		if (this.expiredDt != null) {
			return new Date(this.expiredDt.getTime());
		}
		return null;
	}
	
	public void setExpiredDt(Date expiredDt) {
		this.expiredDt = null;
		if (expiredDt != null) {
			this.expiredDt = new Date(expiredDt.getTime());
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
}
