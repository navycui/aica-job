package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.date;
import lombok.Data;

@Data
public class UsptRsltHist implements Serializable {
	private static final long serialVersionUID = 2149256356379155964L;
	/** 성과이력ID */
	private String rsltHistId;
	/** 성과ID */
	private String rsltId;
	/** 성과상태코드(G:RSLT_STTUS) */
	private String rsltSttusCd;
	/** 성과상태 */
	private String rsltSttus;
	/** 첨부파일그룹ID */
	private String attachmentGroupId;
	/** 제출일시 */
	private Date presentnDt;
	/** 처리내용 */
	private String processCn;
	/** 처리회원유형 */
	private String processMberType;
	/** 처리자명 */
	private String opetrNm;
	/** 로그인ID */
	private String loginId;
	/** 생성자ID */
	@JsonIgnore
	private String creatorId;
	/** 생성일시 */
	@JsonIgnore
	private Date createdDt;

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

	public Date getPresentnDt() {
		if (this.presentnDt != null) {
			return new Date(this.presentnDt.getTime());
         }
		return null;
	}
	public void setPresentnDt(Date presentnDt) {
		this.presentnDt = null;
		if (presentnDt != null) {
			this.presentnDt = new Date(presentnDt.getTime());
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
