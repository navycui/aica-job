package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.date;
import lombok.Data;

@Data
public class FrontRsltListDto implements Serializable {
	private static final long serialVersionUID = -8292207161241179679L;
	/** 성과ID */
	private String rsltId;
	/** 접수번호 */
	private String receiptNo;
	/** 과제명 */
	private String taskNm;
	/** 공고명 */
	private String pblancNm;
	/** 성과상태코드(G:RSLT_STTUS) */
	private String rsltSttusCd;
	/** 성과상태 */
	private String rsltSttus;
	/** 번호 */
	private Long rn;
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
