package aicluster.pms.api.rslt.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class FrontRsltListParam implements Serializable {
	private static final long serialVersionUID = 3637933236698341632L;
	/** 사업연도 */
	private String bsnsYear;
	/** 성과상태코드(G:RSLT_STTUS) */
	private String rsltSttusCd;
	/** 키워드검색 */
	private String keyword;
	/** 키워드검색 구분
	 * (접수번호:receiptNo,과제명:taskNm,공고명:pblancNm)
	 */
	private String keywordDiv;
	/** 회원ID */
	private String memberId;

	private Long beginRowNum;
	private Long itemsPerPage;
}
