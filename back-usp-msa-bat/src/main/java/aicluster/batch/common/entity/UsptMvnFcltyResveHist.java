package aicluster.batch.common.entity;

import java.io.Serializable;
import java.util.Date;

import bnet.library.util.CoreUtils.date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsptMvnFcltyResveHist implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1454177159058532523L;
	private String histId;
	private Date histDt;
	private String reserveId;
	private String workTypeNm;
	private String workCn;
	private String workerId;

	/*
	 * Helper
	 */
	private String workerNm;

	public String getFmtHistDt() {
		if (histDt == null) {
			return "";
		}
		return date.format(histDt, "yyyy-MM-dd HH:mm");
	}
	
	public Date getHistDt() {
		if (this.histDt != null) {
			return new Date(this.histDt.getTime());
		}
		return null;
	}
	
	public void setHistDt(Date histDt) {
		this.histDt = null;
		if (histDt != null) {
			this.histDt = new Date(histDt.getTime());
		}
	}
}
