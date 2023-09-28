package aicluster.pms.api.selection.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class SelectionListParam implements Serializable {
	private static final long serialVersionUID = -3096316361174830545L;
	/** 사업연도 */
	private String bsnsYear;
	/** 처리상태
	 * (ING:대기중, SLCTN:선정완료)
	 */
	private String processSttus;
	/** 통보상태
	 * (ING:대기중, COMPT:통보완료)
	 */
	private String dspthSttus;
	/** 공고명 */
	private String pblancNm;
	/** 키워드검색 */
	private String keyword;
	/** 키워드검색 구분
	 * (담당부서:chrgDeptNm,담당자명:chargerNm)
	 */
	private String keywordDiv;
	/** 로그인한 내부자ID */
	private String insiderId;

	private Long beginRowNum;
	private Long itemsPerPage;

}
