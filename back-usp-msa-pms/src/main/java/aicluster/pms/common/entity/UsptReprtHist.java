package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.date;
import lombok.Data;

@Data
public class UsptReprtHist implements Serializable {
	private static final long serialVersionUID = -8786074350134859255L;
	/** 이력ID */
	@JsonIgnore
	private String histId;
	/** 보고서ID */
	@JsonIgnore
	private String reprtId;
	/** 처리자ID */
	@JsonIgnore
	private String opetrId;
	/** 처리자명 */
	private String opetrNm;
	/** 로그인ID */
	private String loginId;
	/** 보고서상태코드(G:REPRT_STTUS) */
	private String reprtSttusCd;
	/** 보고서상태 */
	private String reprtSttus;
	/** 처리내용 */
	private String processCn;
	/** 생성자ID */
	@JsonIgnore
	private String creatorId;
	/** 생성일시 */
	@JsonIgnore
	private Date createdDt;
	/** 순번 */
	private Integer rn;

	/**
	 * 처리일시
	 * @return
	 */
	public String getProcessDate() {
		if(this.createdDt == null) {
			return "";
		} else {
			return date.format(this.createdDt, "yyyy-MM-dd HH:mm");
		}
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
