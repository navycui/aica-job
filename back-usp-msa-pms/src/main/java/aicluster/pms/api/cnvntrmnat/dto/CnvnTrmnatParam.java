package aicluster.pms.api.cnvntrmnat.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class CnvnTrmnatParam implements Serializable{
	/**
	 *전자협약 해지 관리_admin
	 */
	private static final long serialVersionUID = 9128693978130351246L;

	/** 협약상태코드(G:CNVN_STTUS) */
	String  cnvnSttusCd;
	/**사업년도**/
	String bsnsYear;
	/**협약해지일 시작- 조회조건**/
	String cnvnTrmnatDeStart;
	/**협약해지일 종료- 조회조건**/
	String cnvnTrmnatDeEnd;
	/**접수번호*/
	String receiptNo;
	/** 과제명(국문) */
	String taskNmKo;
	/**회원명(주관업체명) */
	String memberNm;
	/**사업명*/
	String bsnsNm;

	/** 사업협약ID */
	String  bsnsCnvnId;
	/** 신청ID */
	String  applyId;

	private Long beginRowNum;
	private Long itemsPerPage;
}
