package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.date;
import lombok.Data;

@Data
public class FrontReprtListDto implements Serializable {
	private static final long serialVersionUID = -714394272542887788L;
	/** 보고서ID */
	private String reprtId;
	/** 보고서유형코드(G.REPRT_TYPE (I : 중간보고서, F : 최종보고서)) */
	private String reprtTypeCd;
	/** 보고서유형 */
	private String reprtType;
	/** 보고서상태코드(G:REPRT_STTUS) */
	private String reprtSttusCd;
	/** 보고서상태 */
	private String reprtSttus;
	/** 과제명 */
	private String taskNm;
	/** 접수번호 */
	private String receiptNo;
	/** 과제책임자 */
	private String rspnberNm;
	/** 공고명 */
	private String pblancNm;

	/** 제출일 */
	@JsonIgnore
	private Date presentnDt;
	public String getPresentnDate() {
		if(this.presentnDt == null) {
			return "";
		}
		return date.format(this.presentnDt, "yyyy-MM-dd HH:mm");
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

}
