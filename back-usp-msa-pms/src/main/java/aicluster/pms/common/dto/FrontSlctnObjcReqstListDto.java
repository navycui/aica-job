package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.date;
import lombok.Data;

@Data
public class FrontSlctnObjcReqstListDto implements Serializable {
	private static final long serialVersionUID = 2891707124773255287L;
	/** 이의신청ID */
	private String objcReqstId;
	/** 최종선정이의처리상태코드(G:LAST_SLCTN_OBJC_PROCESS_STTUS) */
	private String lastSlctnObjcProcessSttusCd;
	/** 최종선정이의처리상태 */
	private String lastSlctnObjcProcessSttus;
	/** 평가단계명 */
	private String evlStepNm;
	/** 과제명 */
	private String taskNm;
	/** 공고명 */
	private String pblancNm;
	/** 이의신청일시 */
	@JsonIgnore
	private Date objcReqstDt;
	public String getobjcReqstDate() {
		if(this.objcReqstDt == null) {
			return "";
		} else {
			return date.format(this.objcReqstDt, "yyyy-MM-dd HH:mm");
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
