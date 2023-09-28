package aicluster.framework.common.entity;

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
public class LogtIndvdlinfoDwldLog implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1947574170872705767L;

	private String logId;		/** 로그ID */
	private Date   logDt;		/** 로그일시 */
	private String mberId;		/** 회원ID */
	private String mberIp;		/** 회원IP */
	private String opertTyNm;	/** 작업구분명 */
	private String opertCn;		/** 작업내용 */
	private String menuId;		/** 메뉴ID */
	private String menuNm;		/** 메뉴명 */
	private String menuUrl;
	private String apiUrl;
	private String paramtr;
	private String sysIp;
	private String apiSysId;
	private String fileNm;
	
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
