package aicluster.pms.api.evlresult.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class FrontEvlResultListParam implements Serializable {
	private static final long serialVersionUID = 5828226155363242078L;
	/** 평가시작일 */
	private String slctnBgnde;
	/** 평가종료일 */
	private String slctnEndde;
	/** 평가결과 */
	private String slctnResult;
	/** 키워드검색 */
	private String keyword;
	/** 키워드검색 구분
	 * (공고명:pblancNm,과제명:taskNm)
	 */
	private String keywordDiv;
	/** 회원ID */
	private String memberId;

	private Long beginRowNum;
	private Long itemsPerPage;

	/** 평가대상ID */
	private String evlTrgetId;
}
