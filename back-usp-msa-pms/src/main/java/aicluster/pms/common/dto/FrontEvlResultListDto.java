package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.date;
import lombok.Data;

@Data
public class FrontEvlResultListDto implements Serializable {
	private static final long serialVersionUID = 202630211430315442L;
	/** 공고명 */
	private String pblancNm;
	/** 평가단계 */
	private String evlStepNm;
	/** 평가대상ID */
	private String evlTrgetId;
	/** 선정여부 */
	private String slctnAt;
	/** 접수번호 */
	@JsonIgnore
	private String receiptNo;
	/** 과제명 */
	@JsonIgnore
	private String taskNmKo;
	/** 통보경과일 */
	@JsonIgnore
	private int dspthElapseDe;
	/** 선정 이의신청ID */
	@JsonIgnore
	private String slctnObjcReqstId;
	/** 이의신청여부 */
	private String objcReqstAt;
	/** 선정일 */
	@JsonIgnore
	private Date slctnDt;
	/** 순번 */
	private Long rn;
	/**
	 * 평가일
	 * @return
	 */
	public String getEvlDe() {
		if(this.slctnDt == null) {
			return "";
		}
		return date.format(this.slctnDt, "yyyy-MM-dd");
	}

	public Date getSlctnDt() {
		if (this.slctnDt != null) {
			return new Date(this.slctnDt.getTime());
		}
		return null;
	}
	public void setSlctnDt(Date slctnDt) {
		this.slctnDt = null;
		if (slctnDt != null) {
			this.slctnDt = new Date(slctnDt.getTime());
		}
	}

}
