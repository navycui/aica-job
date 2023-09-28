package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class CnvnChangeDto implements Serializable{
	/**
	 * 협약변경관리_admin
	 */
	private static final long serialVersionUID = 4909482977248945548L;

	/** 협약변경상태코드(G:CNVN_CHANGE_STTUS) */
	String  cnvnChangeSttusCd;
	 /** 협약변경상태명 */
	String  cnvnChangeSttusNm;
	/**접수번호*/
	String receiptNo;
	/** 과제명(국문) */
	String taskNmKo;
	 /** 협약변경유형(CNVN_CHANGE_TYPE) */
	String  cnvnChangeTypeCd;
	 /** 협약변경유형명*/
	String  cnvnChangeTypeNm;
	 /** 협약변경항목구분코드(G:CHANGE_IEM_DIV) */
	String  changeIemDivCd;
	 /** 협약변경항목구분명 */
	String  changeIemDivNm;
	/** 신정자ID */
	String memberId;
	/** 신정자명 */
	String memberNm;
	 /** 신청일 */
	String  reqDe;
	/** 협약변경요청ID */
	String  cnvnChangeReqId;
	/** 사업협약ID */
	String  bsnsCnvnId;
	/**사업계획서ID*/
	String bsnsPlanDocId;
	 /** 사업선정대상ID */
	String  bsnsSlctnId;
	 /** 과제책임자명 */
	String rspnberNm;
	 /** email */
	String email;
	 /** 휴대폰번호 */
	String mobileNo;
	/**사업년도*/
	String bsnsYear;
	/**사업명*/
	String bsnsNm;

	Long beginRowNum;
	Long itemsPerPage;

	/** 순번 */
	private Long rn;
}
