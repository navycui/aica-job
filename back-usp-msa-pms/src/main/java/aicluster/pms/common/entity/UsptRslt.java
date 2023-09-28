package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.date;
import lombok.Data;

@Data
public class UsptRslt implements Serializable {
	private static final long serialVersionUID = 3708093286198073381L;
	/** 성과ID */
	private String rsltId;
	/** 신청ID */
	private String applyId;
	/** 성과상태코드(G:RSLT_STTUS) */
	private String rsltSttusCd;
	/** 성과상태 */
	private String rsltSttus;
	/** 보완요청일 */
	private Date makeupReqDe;
	/** 보완요청내용 */
	private String makeupReqCn;
	/** 보완첨부파일그룹ID */
	private String makeupAttachmentGroupId;
	/** 첨부파일그룹ID */
	private String attachmentGroupId;
	/** 제출일시 */
	private Date presentnDt;
	/** 발송방법 */
	private String sndngMth;
	/** 발송내용 */
	private String sndngCn;
	/** 최근발송일 */
	private Date recentSendDate;
	/** 성과년월 */
	private String rsltYm;
	/** 생성자ID */
	@JsonIgnore
	private String creatorId;
	/** 생성일시 */
	@JsonIgnore
	private Date createdDt;
	/** 수정자ID */
	@JsonIgnore
	private String updaterId;
	/** 수정일시 */
	@JsonIgnore
	private Date updatedDt;

	@JsonIgnore
	public String getRsltYear() {
		if(this.createdDt == null) {
			return "";
		}
		return date.format(this.createdDt, "yyyy");
	}

	public Date getMakeupReqDe() {
		if (this.makeupReqDe != null) {
			return new Date(this.makeupReqDe.getTime());
         }
		return null;
	}
	public void setMakeupReqDe(Date makeupReqDe) {
		this.makeupReqDe = null;
		if (makeupReqDe != null) {
			this.makeupReqDe = new Date(makeupReqDe.getTime());
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

	public Date getRecentSendDate() {
		if (this.recentSendDate != null) {
			return new Date(this.recentSendDate.getTime());
         }
		return null;
	}
	public void setRecentSendDate(Date recentSendDate) {
		this.recentSendDate = null;
		if (recentSendDate != null) {
			this.recentSendDate = new Date(recentSendDate.getTime());
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

	public Date getUpdatedDt() {
		if (this.updatedDt != null) {
			return new Date(this.updatedDt.getTime());
		}
		return null;
	}
	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = null;
		if (updatedDt != null) {
			this.updatedDt = new Date(updatedDt.getTime());
		}
	}

}
