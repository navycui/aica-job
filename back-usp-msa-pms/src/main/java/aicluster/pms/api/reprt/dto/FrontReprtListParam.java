package aicluster.pms.api.reprt.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class FrontReprtListParam implements Serializable {
	private static final long serialVersionUID = -1061722305549482236L;
	/** 제출 시작일 */
	private String presentnStartDate;
	/** 제출 종료일 */
	private String presentnEndDate;
	/** 보고서유형코드(G.REPRT_TYPE (I : 중간보고서, F : 최종보고서)) */
	private String reprtTypeCd;
	/** 보고서상태코드(G:REPRT_STTUS) */
	private String reprtSttusCd;
	/** 키워드검색 */
	private String keyword;
	/** 키워드검색 구분
	 * (과제명:taskNm,공고명:pblancNm)
	 */
	private String keywordDiv;
	/** 회원ID */
	private String memberId;

	private Long beginRowNum;
	private Long itemsPerPage;
}
