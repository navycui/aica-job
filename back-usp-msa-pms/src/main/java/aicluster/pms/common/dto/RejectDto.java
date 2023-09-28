package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.date;
import lombok.Data;

@Data
public class RejectDto implements Serializable {
	private static final long serialVersionUID = 6786634485630477039L;
	/** 반려사유내용 */
	private String rejectReasonCn;

	/** 반려일시 */
	@JsonIgnore
	private Date createdDt;
	public String getRejectDate() {
		if(this.createdDt == null) {
			return "";
		}
		return date.format(this.createdDt, "yyyy-MM-dd HH:mm");
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
