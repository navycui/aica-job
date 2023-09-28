package aicluster.pms.api.rslt.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class RsltListParam implements Serializable {
	private static final long serialVersionUID = -4719612382518823039L;
	/** 사업연도 */
	private String bsnsYear;
	/** 성과상태코드(G:RSLT_STTUS) */
	private String rsltSttusCd;
	/** 키워드검색 */
	private String keyword;
	/** 키워드검색 구분
	 * (접수번호:receiptNo,과제명:taskNm,사업명:bsnsNm,사업자명:memberNm)
	 */
	private String keywordDiv;
	/** 로그인한 내부자ID */
	private String insiderId;
	/** 신청ID 목록 */
	List<String> applyIdList;
	/** 제출과제 조회 여부 */
	private String isPresentnTask;

	private Long beginRowNum;
	private Long itemsPerPage;
}
