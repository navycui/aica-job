package aicluster.pms.api.cnvnchange.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class CnvnChangeParam implements Serializable{
	/**
	 * 협약변경관리_admin
	 */
	private static final long serialVersionUID = -8245264403969748728L;

	 /** 협약변경상태코드(G:CNVN_CHANGE_STTUS) */
	String  cnvnChangeSttusCd;
	 /** 협약변경유형(CNVN_CHANGE_TYPE) */
	String  cnvnChangeTypeCd;
	 /** 협약변경항목구분코드(G:CHANGE_IEM_DIV) */
	String  changeIemDivCd;
	/** 신정자명 */
	String memberNm;

	/** 협약변경요청ID */
	String  cnvnChangeReqId;
	/** 사업협약ID */
	String  bsnsCnvnId;
	/** 사업협약변경이력ID*/
	String bsnsCnvnHistId;
	/*사유내용*/
	String resnCn;
	/**사업년도*/
	String bsnsYear;
	/**사업명*/
	String bsnsNm;
	/**과제명*/
	String taskNmKo;
	/**접수번호*/
	String receiptNo;

	/* 이메일*/
	String email;
	/** 휴대폰번호 */
	String mbtlnum;

	/*첨부파일그룹ID*/
	String attachmentGroupId;

	/** 신청자ID */
	String applcntId;

	private Long beginRowNum;
	private Long itemsPerPage;

}
