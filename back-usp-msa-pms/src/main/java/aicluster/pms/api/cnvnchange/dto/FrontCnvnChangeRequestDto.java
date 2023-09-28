package aicluster.pms.api.cnvnchange.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.entity.CmmtCode;
import lombok.Data;

@Data
public class FrontCnvnChangeRequestDto implements Serializable{
	/**
	 * 협약변경관리_front
	 */
	private static final long serialVersionUID = 1110930851996345341L;

	/**사업명*/
	String bsnsNm;
	/**사업년도*/
	String bsnsYear;
	/**접수번호*/
	String receiptNo;
	/** 과제명(국문) */
	String taskNmKo;
	/** 협약일자 */
	String  cnvnDe;
	/** 협약시작일 */
	String  cnvnBgnde;
	/** 협약종료일 */
	String  cnvnEndde;
	/** 협약기간 전체 */
	String  cnvnDePdAll;
	/** 협약상태코드(G:CNVN_STTUS) */
	String  cnvnSttusCd;
	/** 협약상태명*/
	String  cnvnSttusNm;
	/** 사업협약ID */
	String  bsnsCnvnId;
	/** 신청ID */
	String  applyId;
	/** 사업계획서ID */
	String bsnsPlanDocId;

	List<CmmtCode> cmmtCode;

}
