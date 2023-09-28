package aicluster.pms.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils;

@Data
public class SelectionListDto implements Serializable {
	private static final long serialVersionUID = -4686757349416178238L;
	/** 평가최종선정ID */
	private String evlLastSlctnId;
	/** 순번 */
	private Long rn;
	/** 처리상태
	 * (SLCTN:선정완료, ING:대기중)
	 */
	private String processSttus;
	/** 사업연도 */
	private String bsnsYear;
	/** 공고명 */
	private String pblancNm;
	/** 모집유형 */
	private String ordtmRcrit;
	/** 접수차수 */
	private String rceptOdr;
	/** 담당부서 */
	private String chrgDeptNm;
	/** 담당자명 */
	private String chargerNm;
	/** 통보상태
	 * (COMPT:통보완료, ING:대기중)
	 */
	private String dspthSttus;
	/** 최종선정일시 */
	@JsonIgnore
	private Date lastSlctnDt;
	public String getLastSlctnDate() {
		if(this.lastSlctnDt == null) {
			return "";
		}
		return CoreUtils.date.format(this.lastSlctnDt, "yyyy-MM-dd");
	}

	@JsonIgnore
	public String getProcessSttusNm() {
		if(CoreUtils.string.equals("SLCTN", this.processSttus)) {
			return "선정완료";
		} else {
			return "대기중";
		}
	}

	@JsonIgnore
	public String getDspthSttusNm() {
		if(CoreUtils.string.equals("COMPT", this.processSttus)) {
			return "통보완료";
		} else {
			return "대기중";
		}
	}

	public Date getLastSlctnDt() {
		if (this.lastSlctnDt != null) {
			return new Date(this.lastSlctnDt.getTime());
         }
		return null;
	}
	public void setLastSlctnDt(Date lastSlctnDt) {
		this.lastSlctnDt = null;
		if (lastSlctnDt != null) {
			this.lastSlctnDt = new Date(lastSlctnDt.getTime());
		}
	}
}
