package aicluster.pms.common.entity;

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
public class LogtIndvdlinfoLog implements Serializable {
	private static final long serialVersionUID = 625187979768979024L;
	/** 로그ID*/
	private String logId;
	/** 로그시각 */
	private Date logDt;
	/** 회원ID */
	private String memberId;
	/** 회원IP */
	private String memberIp;
	/** 작업구분명 */
	private String workTypeNm;
	/** 작업내용 */
	private String workCn;
	/** 대상자ID */
	private String trgterId;

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
