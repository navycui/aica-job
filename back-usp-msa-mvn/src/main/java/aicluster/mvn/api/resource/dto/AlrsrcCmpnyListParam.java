package aicluster.mvn.api.resource.dto;

import java.io.Serializable;
import java.util.Date;

import bnet.library.util.CoreUtils.date;
import lombok.Data;

@Data
public class AlrsrcCmpnyListParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1280602157186030732L;

	private String bsnsYear     ;      /* 사업년도                                     */
	private String alrsrcSt     ;      /* 자원할당상태(G:ALRSRC_ST)                    */
	private Date alrsrcBgngDay;      /* 자원할당시작일                               */
	private Date alrsrcEndDay ;      /* 자원할당종료일                               */
	private Boolean ordtmRcrit  ;      /* 상시모집여부(true:상시모집, false:정시모집)  */
	private String searchType   ;      /* 검색유형(CMPNY_NM:사업자명(이름), RECEIPT_NO:접수번호, PBLANC_NM:공고명) */
	private String searchCn     ;      /* 검색내용 */

	public String getAlrsrcBgngDay() {
		if (this.alrsrcBgngDay == null) {
			return null;
		}
		return date.format(this.alrsrcBgngDay, "yyyyMMdd");
	}

	public String getAlrsrcEndDay() {
		if (this.alrsrcEndDay == null) {
			return null;
		}
		return date.format(this.alrsrcEndDay, "yyyyMMdd");
	}
}
