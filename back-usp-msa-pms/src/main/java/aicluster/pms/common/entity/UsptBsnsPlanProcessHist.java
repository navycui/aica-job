package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class UsptBsnsPlanProcessHist implements Serializable {
	/**
	 *	사업계획서처리이력
	 */
	private static final long serialVersionUID = 992958695848229400L;
	/** 사업계획처리이력ID */
	String  planProcessHistId;
	/** 사업계획서ID */
	String  bsnsPlanDocId;
	/** 사업계획제출상태코드(G:PLAN_PRESENTN_STTUS) */
	String  planPresentnSttusCd;
	/** 사업계획제출상태명 */
	String  planPresentnSttusNm;
	/** 사유내용 */
	String  resnCn;
	/** 처리자id*/
	String  opetrId;
	/** 처리자명*/
	String  opetrNm;
	/** String_처리일시 */
	String  stProcessDt;
	/** 순번 */
	int rn;

	/** 페이징 처리 */
	Long beginRowNum;

	Long itemsPerPage;
	/** 생성자 ID */
	String creatorId;
	/** 생성일시 */
	Date createdDt;
}
