package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class EvlPlanListDto implements Serializable {

	private static final long serialVersionUID = -7203802550961736184L;

	private int rn;					/** 순번 */

	String  evlPlanId;              /** 평가계획ID */
	String  pblancId;               /** 공고ID */
	Integer rceptOdr;               /** 접수차수 */

	Boolean wctMdatDlbrt;           /** 사업비조정심의여부 */
	String  evlSttusCd;             /** 평가진행상태코드(G:EVL_STTUS) */
	String  evlSttusNm;             /** 평가진행상태명(G:EVL_STTUS) */
	String  evlTypeCd;              /** 평가유형코드(G:EVL_TYPE) */
	String  evlTypeNm;              /** 평가유형명(G:EVL_TYPE) */

	String  pblancNm;               /** 공고명 */
	String  pblancNo;               /** 공고번호 */
	Boolean ordtmRcrit; 	        /** 상시모집여부 */
	String ordtmRcritNm; 	        /** 상시모집여부명 */

	String evlPrarnde;           	/** 평가예정일시 */
	String rgsde;				    /** 등록일 */

	private String chrgDeptNm;		/** 담당부서명 */
	private String chargerNm;		/** 담당자명 */
	private String insiderId;		/** 담당자ID */

	/** 엑셀 다운로드 여부 */
	private boolean isExcel;

	/** 페이징 처리 */
	private Long beginRowNum;
	private Long itemsPerPage;
	private Long totalItems;

}
