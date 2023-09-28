package aicluster.pms.api.bsns.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class BsnsListParam implements Serializable {

	private static final long serialVersionUID = -7437164127087027133L;

	/** 사업년도 */
	private String bsnsYear;
	/** 사업년도상세코드 */
	private String bsnsYearDetailCd;
	/** 사업명 */
	private String bsnsNm;
	/** 담당부서명 */
	private String chrgDeptNm;
	/** 담당자명 */
	private String chargerNm;

	/** 키워드검색 구분
	 * (bsnsCd:사업코드, stdrBsnsCd:기준사업코드, stdrBsnsNm: 기준사업명)
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
