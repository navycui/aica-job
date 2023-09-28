package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils;
import lombok.Data;

@Data
public class EvlTargetListDto implements Serializable {
	private static final long serialVersionUID = -8867532740478413836L;
	/** 평가위원회ID */
	private String evlCmitId;
	/** 평가위원회명 */
	private String evlCmitNm;
	@JsonIgnore
	/** 평가상태코드 */
	private String evlSttusCd;
	/** 평가상태명 */
	private String EvlSttusNm;
	/** 평가예정일  */
	private String evlPrarnde;
	/** 간사ID */
	private String orgnzrId;
	/** 간사명 */
	private String orgnzrNm;
	/** 사업연도 */
	private String bsnsYear;
	/** 공고명 */
	private String pblancNm;
	@JsonIgnore
	/** 평가유형코드 */
	private String evlTypeCd;
	/** 평가유형명 */
	private String EvlTypeNm;
	/** 평가단계ID */
	@JsonIgnore
	private String evlStepId;
	/** 평가단계명 */
	private String evlStepNm;
	/** 선정대상수 */
	private Integer slctnTrgetCo;
	/** 분과명 */
	private String sectNm;
	/** 모집유형명 */
	private String ordtmRcritNm;
	/** 접수차수 */
	private String rceptOdr;
	/** 평가방식코드(G:EVL_METHOD) */
	private String evlMthdCd;
	/** 순번 */
	private Long rn;
	/** 담당자권한코드 */
	@JsonIgnore
	private String chargerAuthorCd;
	/** 사업명 */
	@JsonIgnore
	private String bsnsNm;
	/** 담당부서 */
	@JsonIgnore
	private String chrgDeptNm;
	/** 평가장소 */
	@JsonIgnore
	private String evlPlace;
	@JsonIgnore
	/** 통보상태명
	 * (완료:'COMPT', 대기중:'ING')
	 */
	private String dspthSttus;
	public String getDspthSttusNm() {
		if(CoreUtils.string.equals("COMPT", this.dspthSttus)) {
			return "통보완료";
		} else if(CoreUtils.string.equals("ING", this.dspthSttus)) {
			return "대기중";
		} else {
			return "";
		}
	}

	@JsonIgnore
	/** 평가방법 */
	private String evlMth;
	public String getEvlMthNm() {
		if(CoreUtils.string.equals("ON", this.evlMth)) {
			return "온라인";
		} else {
			return "오프라인";
		}
	}

	@JsonIgnore
	/** 평가상태일시 */
	private Date evlSttusDt;
	public String getEvlSttusDate() {
		if(this.evlSttusDt == null) {
			return "";
		}
		return CoreUtils.date.format(this.evlSttusDt, "yyyy-MM-dd HH:mm");
	}

	public Date getEvlSttusDt() {
		if (this.evlSttusDt != null) {
			return new Date(this.evlSttusDt.getTime());
         }
		return null;
	}
	public void setEvlSttusDt(Date evlSttusDt) {
		this.evlSttusDt = null;
		if (evlSttusDt != null) {
			this.evlSttusDt = new Date(evlSttusDt.getTime());
		}
	}
}
