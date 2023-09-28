package aicluster.mvn.api.resource.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import bnet.library.util.CoreUtils.date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlrsrcCmpnyInsListItem implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2216684538643661715L;

	private String lastSlctnTrgetId;		/** 최종선정대상ID (USPT_BSNS_SLCTN.LAST_SLCTN_TRGET_ID) */
	private String bsnsSlctnId;		/** 선정대상ID (USPT_BSNS_SLCTN.BSNS_SLCTN_ID) */
	private String receiptNo;		/** 접수번호 (USPT_BSNS_PBLANC_APPLCNT.RECEIPT_NO) */
	private String memberId;		/** 회원ID */
	private Date bsnsBgnde;			/** 사업시작일 (USPT_BSNS_PBLANC.BSNS_BGNDE) */
	private Date bsnsEndde;			/** 사업종료일 (USPT_BSNS_PBLANC.BSNS_ENDDE) */

	private List<AlrsrcDstbInsListItem> alrsrcDstbInsList;		/** 자원할당 입력목록 */

	public String getBsnsBgnde() {
		if (this.bsnsBgnde == null) {
			return null;
		}
		return date.format(this.bsnsBgnde, "yyyyMMdd");
	}

	public String getBsnsEndde() {
		if (this.bsnsEndde == null) {
			return null;
		}
		return date.format(this.bsnsEndde, "yyyyMMdd");
	}
}
