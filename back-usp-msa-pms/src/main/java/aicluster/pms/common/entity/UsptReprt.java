package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UsptReprt implements Serializable {
	private static final long serialVersionUID = -6846163879596912779L;
	/** 보고서ID */
	private String reprtId;
	/** 최종선정대상ID */
	private String lastSlctnTrgetId;
	/** 보고서유형코드(G.REPRT_TYPE (I : 중간보고서, F : 최종보고서)) */
	private String reprtTypeCd;
	/** 보고서상태코드(G:REPRT_STTUS) */
	private String reprtSttusCd;
	/** 보고서 요약내용 */
	private String reprtSumryCn;
	/** 첨부파일그룹ID */
	private String attachmentGroupId;
	/** 보완요청일 */
	private Date makeupReqDe;
	/** 보완요청내용 */
	private String makeupReqCn;
	/** 보완첨부파일그룹ID */
	private String makeupAttachmentGroupId;
	/** 제출일시 */
	private Date presentnDt;
	/** 발송방법 */
	private String sndngMth;
	/** 발송내용 */
	private String sndngCn;
	/** 최근발송일 */
	private Date recentSendDate;

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
