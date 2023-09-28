package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.date;
import lombok.Data;

@Data
public class UsptBsnsPblancApplcntHist implements Serializable {
	private static final long serialVersionUID = 1866332385837134118L;
	/** 이력ID */
	@JsonIgnore
	private String histId;
	/** 신청ID */
	@JsonIgnore
	private String applyId;
	/** 처리자ID */
	@JsonIgnore
	private String opetrId;
	/** 처리자명 */
	private String opetrNm;
	/** 로그인 ID */
	private String loginId;
	/** 접수상태코드(G:RCEPT_STTUS) */
	private String rceptSttusCd;
	/** 접수상태 */
	private String rceptSttus;
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
		String processDate = "";
		if(this.createdDt == null) {
			return processDate;
		}
		processDate = date.format(this.createdDt, "yyyy-MM-dd HH:mm");
		return processDate;
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
