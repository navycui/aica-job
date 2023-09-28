package aicluster.framework.common.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CmmtEmailRcver implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -3908758167569233909L;
	private String emailId;
	private String email;
	private String recipientNm;
	private String param;
	private Boolean success;
	private Integer resultCode;
	private String resultMessage;
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
