package aicluster.pms.api.evl.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class EvlSystemParam implements Serializable {

	/**
	 * 평가시스템
	 */
	private static final long serialVersionUID = -2529824762650336071L;

	/** 평가위원회ID */
	private String evlCmitId;
	/** 평가위원ID */
	private String evlMfcmmId;
	/** 평가위원명 */
	private String evlMfcmmNm;
	/** 체크리스트여부 */
	private Boolean chklstChkAt;
	/** 첨부파일그룹ID */
	private String  attachmentGroupId;
	/** 평가위원장종합의견내용 */
	private String evlCharmnOpinionCn;
	/** 약관동의 세션ID */
	private String sessionId;
	/** 나의 평가완료 여부 */
	private String myCompleteYn;
	/** 평가위원 완료 여부 */
	private String evlMfcmmCompleteYn;
	/** 평가위원휴대폰번호 */
	private String evlMfcmmMbtlnum;
	/** 평가위원Email*/
	private String  evlMfcmmEmail;
}
