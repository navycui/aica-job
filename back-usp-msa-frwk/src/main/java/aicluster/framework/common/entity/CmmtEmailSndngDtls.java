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
public class CmmtEmailSndngDtls implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -939912996281258689L;
	private String emailId;
	private String title;
	private String emailCn;
	private Boolean html;
	private String senderEmail;
	private String senderNm;
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
