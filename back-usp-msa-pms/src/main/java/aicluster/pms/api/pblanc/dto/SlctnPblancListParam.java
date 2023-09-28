package aicluster.pms.api.pblanc.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class SlctnPblancListParam implements Serializable {
	private static final long serialVersionUID = -3648054065012738957L;
	/** 사업연도 */
	private String bsnsYear;
	/** 사업명 */
	private String bsnsNm;
	/** 포털게시상태 */
	private Boolean ntce;
	/** 담당부서명 */
	private String chrgDeptNm;
	/**
	 * 키워드검색 구분
	 * (pblancNm:공고명)
	 */
	private String keywordDiv;
	/** 키워드검색 조건 */
	private String keyword;
	/** 엑셀 다운로드 여부 */
	private Boolean isExcel;
	/** 로그인 사용자 */
	private String insiderId;

	private Long beginRowNum;
	private Long itemsPerPage;
}
