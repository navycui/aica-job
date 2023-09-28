package aicluster.pms.api.slctnObjc.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class FrontSlctnObjcReqstListParam implements Serializable {
	private static final long serialVersionUID = -5684696523804022275L;
	/** 이의신청 시작일 */
	private String objcReqstStartDate;
	/** 이의신청 종료일 */
	private String objcReqstEndDate;
	/** 최종선정이의처리상태코드(G:LAST_SLCTN_OBJC_PROCESS_STTUS) */
	private String lastSlctnObjcProcessSttusCd;

	/** 키워드검색 */
	private String keyword;
	/** 키워드검색 구분
	 * (과제명:taskNm,공고명:pblancNm)
	 */
	private String keywordDiv;
	/** 회원ID */
	private String memberId;
	/** 이의신청ID */
	private String objcReqstId;

	private Long beginRowNum;
	private Long itemsPerPage;
}
