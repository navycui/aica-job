package aicluster.pms.api.evl.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class FrontEvlPresnatnParam implements Serializable {

	/**
	 * 발료관리 조회 front
	 */
	private static final long serialVersionUID = 3101741370964539994L;

	/** 회원ID */;
	String  memberId;
	/** 제출 시작일 */
	String    presnatnBgnde;
	/** 제출 종료일 */
	String    presnatnEndde;
	/** 발표자료처리구분코드(G:PRESNATN_PROCESS_DIV) */
	String  presnatnProcessDivCd;
	/** 공고명 */
	String  pblancNm;
	/** 과제명(국문) */
	String  taskNmKo;
	/** 평가대상ID */
	String  evlTrgetId;

	private Long beginRowNum;
	private Long itemsPerPage;
}
