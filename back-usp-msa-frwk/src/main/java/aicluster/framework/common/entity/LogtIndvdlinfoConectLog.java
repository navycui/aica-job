package aicluster.framework.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogtIndvdlinfoConectLog implements Serializable {

	private static final long serialVersionUID = 267810968012200398L;

	private String logId;
	private Date logDt;
	private String memberId;
	private String memberIp;
	private String workTypeNm;
	private String workCn;
	private String menuUrl;
	private String apiUrl;
	private String paramtr;
	private String sysIp;
	private String apiSysId;
	private String trgterId;
	private String trgterNm;	/** 대상자명 */
	@JsonIgnore
	private String encptEmail;		/** 이메일 */
	@JsonIgnore
	private String encptBrthdy;		/** 생년월일(yyyyMMdd) */
	@JsonIgnore
	private String encptMbtlnum;	/** 휴대폰번호 */

	/**
	 * 암호화된 이메일을 복호화 리턴
	 *
	 * @return 이메일
	 */
	public String getEmail() {
		if ( string.isBlank(this.encptEmail) ) {
			return null;
		}

		return aes256.decrypt(this.encptEmail, this.trgterId);
	}

	/**
	 * 암호화된 생년월일을 복호화 리턴
	 *
	 * @return 생년월일
	 */
	public String getBirthday() {
		if ( string.isBlank(this.encptBrthdy) ) {
			return null;
		}

		return aes256.decrypt(this.encptBrthdy, this.trgterId);
	}

	/**
	 * 암호화된 휴대폰 번호를 복호화 리턴
	 *
	 * @return 휴대폰 번호
	 */
	public String getMobileNo() {
		if ( string.isBlank(this.encptMbtlnum) ) {
			return null;
		}

		return aes256.decrypt(this.encptMbtlnum, this.trgterId);
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
