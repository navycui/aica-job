package aicluster.pms.api.bsnsplan.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class FrontBsnsPlanParam implements Serializable{

	/**
	 *사업계획서 접수관리_front
	 */
	private static final long serialVersionUID = -8486555560906943620L;
	/**제출일시작**/
	String presentnDtStart;
	/**제출일종료**/
	String presentnDtEnd;
	/** 사업계획제출상태코드(G:PLAN_PRESENTN_STTUS) */
	String planPresentnSttusCd;
	/** 과제명(국문) */
	String taskNmKo;
	/** 공고명 */
	String pblancNm;
	/** 접수번호(BA + 8자리 순번) */
	String receiptNo;
	/**사업년도*/
	String bsnsYear;
	/**사업명*/
	String bsnsNm;
	/** 책임자명_회원명 */
	String rspnberNm;
	/** 신청자명 */
	String memberNm;
	/** 신청자ID */
	String memberId;

	 /** 사업계획서ID */
	String  bsnsPlanDocId;
	/** 사업선정대상ID */
	String  bsnsSlctnId;
	/** 과제신청사업비ID */
	String  taskReqstWctId;
	/** 보완요청파일그룹ID */
	String  makeupReqFileGroupId;
	/** 제출첨부파일그룹ID */
	String  prsentrAttachmentGroupId;

	private Long beginRowNum;
	private Long itemsPerPage;

}
