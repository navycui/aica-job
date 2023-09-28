package aicluster.pms.api.slctnObjc.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.date;
import lombok.Data;

@Data
public class SlctnObjcReqst implements Serializable {
	private static final long serialVersionUID = -5915753173999587762L;
	/** 이의신청내용 */
	private String objcReqstCn;
	/** 이의신청자ID */
	@JsonIgnore
	private String objcApplcntId;
	/** 최종선정이의처리상태코드(G:LAST_SLCTN_OBJC_PROCESS_STTUS) */
	private String lastSlctnObjcProcessSttusCd;
	/** 최종선정이의처리상태 */
	private String lastSlctnObjcProcessSttus;
	/** 심의일 */
	private String dlbrtDe;
	/** 심의내용 */
	private String dlbrtCn;
	/** 심의처리자ID */
	@JsonIgnore
	private String dlbrtOpetrId;
	/** 신청자첨부파일그룹ID */
	@JsonIgnore
	private String applcntAttachmentGroupId;
	/** 심의결과첨부파일그룹ID */
	@JsonIgnore
	private String dlbrtAttachmentGroupId;
	/** 반려사유 내용 */
	private String rejectReasonCn;
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


	/** 이의신청일시 */
	@JsonIgnore
	private Date objcReqstDt;
	public String getObjcReqstDate() {
		if(this.objcReqstDt == null) {
			return "";
		} else {
			return date.format(this.objcReqstDt, "yyyy-MM-dd HH:mm");
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

	public Date getObjcReqstDt() {
		if (this.objcReqstDt != null) {
			return new Date(this.objcReqstDt.getTime());
         }
		return null;
	}
	public void setObjcReqstDt(Date objcReqstDt) {
		this.objcReqstDt = null;
		if (objcReqstDt != null) {
			this.objcReqstDt = new Date(objcReqstDt.getTime());
		}
	}

}
