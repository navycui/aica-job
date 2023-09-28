package aicluster.common.common.entity;

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
public class LogtIndvdlinfoConectLog implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -2421174334074882764L;
	private String logId;
	private Date logDt;
	private String memberId;
	private String memberIp;
	private String workTypeNm;
	private String workCn;
	private String trgterId;

	/*
	 * Helper
	 */
	private String memberNm;
	private String trgterNm;
	private Long rn;

	public String getFmtLogDt() {
		if (this.logDt == null) {
			return null;
		}

		return date.format(this.logDt, "yyyy-MM-dd HH:mm:ss");
	}
	
	public Date getLogDt() {
		if (this.logDt != null) {
			return new Date(this.logDt.getTime());
		}
		return null;
	}
	
	public void setLogDt(Date logDt) {
		this.logDt = null;
		if (logDt != null) {
			this.logDt = new Date(logDt.getTime());
		}
	}
}
