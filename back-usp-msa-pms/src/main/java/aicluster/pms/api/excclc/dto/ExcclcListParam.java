package aicluster.pms.api.excclc.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ExcclcListParam implements Serializable {
	private static final long serialVersionUID = -5252446657311385430L;
	/** 사업연도 */
	private String bsnsYear;
	/** 처리상태 (대기중:I, 등록완료:C) */
	private String processSttus;
	/** 키워드검색 */
	private String keyword;
	/** 키워드검색 구분
	 * (접수번호:receiptNo,과제명:taskNm,사업명:bsnsNm,사업자명:memberNm, 사업자등록번호:bizrno)
	 */
	private String keywordDiv;
	/** 로그인한 내부자ID */
	private String insiderId;

	private Long beginRowNum;
	private Long itemsPerPage;
}
