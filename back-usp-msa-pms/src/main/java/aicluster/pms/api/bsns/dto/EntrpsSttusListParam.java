package aicluster.pms.api.bsns.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class EntrpsSttusListParam implements Serializable {
	private static final long serialVersionUID = 839656753061995759L;
	/** 사업년도 */
	private String bsnsYear;
	/** 업체명 */
	private String entrpsNm;
	/** 사업자등록번호 */
	private String bizrno;
	/** 참여구분
	 * 주관업체(M)/참여업체(P) */
	private String partcptnDiv;

	/** 키워드검색 구분
	 * (bsnsNm:사업명, pblancNm:공고명, taskNm:과제명)
	 */
	private String keywordDiv;
	/** 키워드검색 조건 */
	private String keyword;
	/** 엑셀 다운로드 여부 */
	private Boolean isExcel;
	/** 로그인 사용자 */
	private String insiderId;
	/** 엑셀다운 작업내용 */
	private String excelDwldWorkCn;

	private Long beginRowNum;
	private Long itemsPerPage;
}
