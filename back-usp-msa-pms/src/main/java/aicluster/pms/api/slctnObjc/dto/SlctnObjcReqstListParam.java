package aicluster.pms.api.slctnObjc.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class SlctnObjcReqstListParam implements Serializable {
	private static final long serialVersionUID = 1452569975184317212L;
	/** 사업연도 */
	private String bsnsYear;
	/** 최종선정이의처리상태코드(G:LAST_SLCTN_OBJC_PROCESS_STTUS) */
	private String lastSlctnObjcProcessSttusCd;
	/** 키워드검색 */
	private String keyword;
	/** 키워드검색 구분
	 * (회원명:memberNm, 공고명:pblancNm, 담당부서:deptNm, 담당자명:chargerNm)
	 */
	private String keywordDiv;
	/** 관리자ID */
	private String insiderId;

	private Long beginRowNum;
	private Long itemsPerPage;
}
