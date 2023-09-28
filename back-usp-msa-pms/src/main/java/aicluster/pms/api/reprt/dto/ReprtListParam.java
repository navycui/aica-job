package aicluster.pms.api.reprt.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class ReprtListParam implements Serializable {
	private static final long serialVersionUID = 2347916762333798696L;
	/** 사업연도 */
	private String bsnsYear;
	/** 처리상태 */
	private String reprtSttusCd;
	/**
	 * 보고서유형코드(G.REPRT_TYPE) :
	 * I : 중간보고서, F : 최종보고서
	 */
	private String reprtTypeCd;
	/** 키워드검색 */
	private String keyword;

	/** 키워드검색 구분
	 * (접수번호:receiptNo,과제명:taskNm,사업명:bsnsNm,사업자명:memberNm)
	 */
	private String keywordDiv;
	/** 로그인한 내부자ID */
	private String insiderId;
	/** 최종선정대상ID 목록 */
	List<String> trgetIdList;

	private Long beginRowNum;
	private Long itemsPerPage;
}
