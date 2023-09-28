package aicluster.pms.api.cnvnchange.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class FrontCnvnChangeParam implements Serializable{
	/**
	 * 협약변경관리_front
	 */
	private static final long serialVersionUID = -3664558565406170605L;

	/**사업년도**/
	String bsnsYear;
	/** 과제명(국문) */
	String taskNmKo;
	/** 사업계획서ID */
	String bsnsPlanDocId;
	 /** 협약변경항목구분코드(G:CHANGE_IEM_DIV) */
	String  changeIemDivCd;
	/** 신정자ID */
	String memberId;

	/** 협약변경요청ID */
	String  cnvnChangeReqId;
	/** 사업협약ID */
	String  bsnsCnvnId;
	/** 사업협약변경이력ID*/
	String bsnsCnvnHistId;

	 /** 협약변경상태코드(G:CNVN_CHANGE_STTUS) */
	String  cnvnChangeSttusCd;

	private Long beginRowNum;
	private Long itemsPerPage;

}
