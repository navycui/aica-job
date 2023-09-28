package aicluster.pms.api.stdnt.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class StdntCareerParam implements Serializable {
	private static final long serialVersionUID = 8946765412891214404L;
	/** 사업연도 */
	private String bsnsYear;
	/** 희망직무코드 (G:HOPE_DTY) */
	private String hopeDtyCd;
	/** 매칭여부 (WAIT:대기중, COMPT:완료)*/
	private String mtchAt;
	/** 키워드 검색 */
	private String keyword;
	/** 키워드 검색 구분 (receiptNo:접수번호, memberNm:이름) */
	private String keywordDiv;
	/** 내부사용자ID */
	private String insiderId;

	/** 신청ID 목록 */
	List<String> applyIdList;

	private Long beginRowNum;
	private Long itemsPerPage;
}
