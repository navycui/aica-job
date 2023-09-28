package aicluster.pms.api.cnvntrmnat.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import lombok.Data;

@Data
public class CnvnTrmnatInputParam implements Serializable{
	/**
	 *전자협약 해지 관리_admin
	 */

	private static final long serialVersionUID = -7581478770694765834L;
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

	/**협약해지일**/
	String cnvnTrmnatDe;
	 /** 협약해지사유구분코드 */
	String  cnvnTrmnatResnCnDivCd;
	/** 협약해지사유내용 */
	String  cnvnTrmnatResnCn;
	/** 환수대상금액 */
	Long    redempTrgamt;
	/** 환수완료금액 */
	Long    redempComptAmount;
	/** 환수일 */
	String  redempDe;
	 /** 협약해지첨부파일그룹ID */
	String  cnvnTrmnatFileGroupId;

	 /** 협약해지처리구분코드 */
	String  cnvnTrmnatProcessDivCd;
	 /** 사유내용 */
	String  resnCn;

	/** 신정자ID(주사업자) */
	String memberId;

	/** 첨부파일삭제 List */
	List<CmmtAtchmnfl> attachFileDeleteList;

}
