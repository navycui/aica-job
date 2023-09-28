package aicluster.pms.api.bsnsapply.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class BsnsApplyListParam implements Serializable {
	private static final long serialVersionUID = -4721975782673586481L;
	/** 사업연도 */
	private String bsnsYear;
	/** 사업명 */
	private String bsnsNm;
	/** 공고명 */
	private String pblancNm;
	/** 접수상태코드(G:RCEPT_STTUS) */
	private String rceptSttusCd;
	/** 키워드 검색 */
	private String keyword;
	/** 키워드 검색 구분 (receiptNo:접수번호, memberNm:회원명, taskNm:과제명) */
	private String keywordDiv;
	/** 정렬 */
	private String sortOrder;
	/** 상시접수여부 */
	private Boolean ordtmRcrit;
	/** 로그인 사용자 */
	private String insiderId;

	private Long beginRowNum;
	private Long itemsPerPage;
}
