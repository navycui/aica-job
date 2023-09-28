package aicluster.pms.api.cnvnchangehist.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class CnvnChangeHistParam implements Serializable{
	/**
	 * 협약변경내역 이력
	 */
	private static final long serialVersionUID = -2471311946044745887L;

	/**사업년도*/
	String bsnsYear;
	/**사업명*/
	String bsnsNm;
	/**접수번호*/
	String receiptNo;
	/**과제명*/
	String taskNmKo;
	/** 신정자명 */
	String memberNm;

	 /** 협약변경항목구분코드(G:CHANGE_IEM_DIV) */
	String  changeIemDivCd;
	/** 협약변경요청ID */
	String  cnvnChangeReqId;
	/** 사업협약ID */
	String  bsnsCnvnId;

	/** 생성일시 */
	String changeDt;

	private Long beginRowNum;
	private Long itemsPerPage;

}
