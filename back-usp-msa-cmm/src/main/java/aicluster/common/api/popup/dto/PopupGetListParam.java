package aicluster.common.api.popup.dto;

import java.io.Serializable;
import java.util.Date;

import bnet.library.util.CoreUtils.date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopupGetListParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 4023596074021454246L;

	private String systemId;
	private Boolean posting;
	private String title;
	private Date beginDt;
	private Date endDt;

	public String getBeginDt() {
		if (this.beginDt == null) {
			return null;
		}
		return date.format(this.beginDt, "yyyyMMdd");
	}
	
	public void setBeginDt(Date beginDt) {
		this.beginDt = null;
		if (beginDt != null) {
			this.beginDt = new Date(beginDt.getTime());
		}
	}

	public String getEndDt() {
		if (this.endDt == null) {
			return null;
		}
		return date.format(this.endDt, "yyyyMMdd");
	}
	
	public void setEndDt(Date endDt) {
		this.endDt = null;
		if (endDt != null) {
			this.endDt = new Date(endDt.getTime());
		}
	}
}
