package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils;
import lombok.Data;

@Data
public class SlctnDetailDto implements Serializable {
	private static final long serialVersionUID = -1830129813465429014L;
	/** 평가최종선정ID */
	private String evlLastSlctnId;
	/** 최종선정여부 */
	private Boolean lastSlctn;
	/** 최종선정처리구분코드(G:LAST_SLCTN_PROCESS_DIV) */
	private String lastSlctnProcessDivCd;
	/** 최종선정처리구분 */
	private String lastSlctnProcessDiv;
	/** 통보상태 */
	private String dspthSttus;
	/** 사업명 */
	private String bsnsNm;
	/** 사업연도 */
	private String bsnsYear;
	/** 공고명 */
	private String pblancNm;
	/** 공고번호 */
	private String pblancNo;
	/** 모집유형 */
	private String ordtmRcrit;
	/** 접수차수 */
	private String rceptOdr;
	/** 담당부서 */
	private String chrgDeptNm;
	/** 담당자 */
	private String chargerNm;
	/** 사업비조정심의여부 */
	private Boolean wctMdatDlbrt;
	/** 사업유형코드(G:BSNS_TYPE) */
	private String bsnsTypeCd;
	/** 담당자권한코드 */
	@JsonIgnore
	private String chargerAuthorCd;

	/** 최종선정일시 */
	@JsonIgnore
	private Date lastSlctnDt;
	public String getLastSlctnDate() {
		if(this.lastSlctnDt == null) {
			return "";
		}

		return CoreUtils.date.format(this.lastSlctnDt, "yyyy-MM-dd");
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
