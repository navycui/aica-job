package aicluster.mvn.common.dto;

import java.io.Serializable;
import java.util.List;

import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import lombok.Data;

@Data
public class AlrsrcCmpnySlctnDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3373777889348206039L;

	private String alrsrcId					;	/** 자원할당ID                                    */
	private String evlLastSlctnId			;	/** 평가최종선정ID                */
	private String lastSlctnTrgetId			;	/** 최종선정대상ID                                */
	private String bsnsSlctnId				;	/** 사업선정대상ID                                */
	private String receiptNo				;	/** 접수번호(USPT_BSNS_PBLANC_APPLCNT.RECEIPT_NO) */
	private String cmpnyId					;	/** 업체ID                                        */
	private String cmpnyNm					;	/** 업체명                                        */
	private String alrsrcBgngDay			;	/** 자원할당시작일                                */
	private String alrsrcEndDay				;	/** 자원할당종료일                                */
	private String alrsrcSt					;	/** 자원할당상태                                  */
	private String alrsrcStNm				;	/** 자원할당상태명                                */

	private List<AlrsrcFirstDstbDto> firstDstbList;	/** 최초 배분 목록                            */

	public String getFmtAlrsrcBgngDay() {
		if (string.isBlank(this.alrsrcBgngDay) || !date.isValidDate(this.alrsrcBgngDay, "yyyyMMdd")) {
			return null;
		}
		return date.format(string.toDate(this.alrsrcBgngDay), "yyyy-MM-dd");
	}

	public String getFmtAlrsrcEndDay() {
		if (string.isBlank(this.alrsrcEndDay) || !date.isValidDate(this.alrsrcEndDay, "yyyyMMdd")) {
			return null;
		}
		return date.format(string.toDate(this.alrsrcEndDay), "yyyy-MM-dd");
	}
}
