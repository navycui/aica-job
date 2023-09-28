package aicluster.pms.api.bsnsapply.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class FrontBsnsApplyListParam implements Serializable{
	private static final long serialVersionUID = 8097119330091697860L;
	/** 신청일 시작일 */
	private String rceptDtStart;
	/** 신청일 종료일 */
	private String rceptDtEnd;
	/** 신청상태 */
	private String rceptSttusCd;
	/** 검색 키워드 구분 (taskNm:과제명, pblancNm: 공고명) */
	private String keywordDiv;
	/** 검색 키워드 */
	private String keyword;
	/** 회원ID */
	private String memberId;

	private Long beginRowNum;
	private Long itemsPerPage;

}
