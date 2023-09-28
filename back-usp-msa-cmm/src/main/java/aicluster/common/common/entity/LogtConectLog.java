package aicluster.common.common.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogtConectLog implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -4280761094814735061L;
	private String logId;
	private String apiSystemId;
	private Date logDt;
	private String url;
	private String memberId;
	private String loginId;
	private String memberNm;
	private String memberType;
	private String gender;
	private Integer age;
	private String memberIp;
	private String positionNm;
	private String deptNm;
	private String pstinstNm;
	
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
