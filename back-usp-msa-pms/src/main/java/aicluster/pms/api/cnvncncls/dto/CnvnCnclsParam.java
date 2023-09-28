package aicluster.pms.api.cnvncncls.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class CnvnCnclsParam implements Serializable{
	/**
	 *전자협약 관리_admin
	 */
	private static final long serialVersionUID = -4174122207003301938L;
	/**사업년도**/
	String bsnsYear;
	/**협약일자**/
	String cnvnDe;
	/**협약시작일**/
	String cnvnBgnde;
	/**협약종료일**/
	String cnvnEndde;
	/**협약서본문*/
	String cnvnBdt;
	/**협약일자시작- 조회조건**/
	String cnvnDeStart;
	/**협약일자종료- 조회조건**/
	String cnvnDeEnd;
	/** 협약상태코드(G:CNVN_STTUS) */
	String  cnvnSttusCd;
	/** 과제명(국문) */
	String taskNmKo;
	/**사업명*/
	String bsnsNm;
	/**회원ID*/
	String memberId;
	/**회원명(주관업체명) */
	String memberNm;
	/**과제책임자명 */
	String rspnberNm;
	/** 사업협약ID */
	String  bsnsCnvnId;
	/** 신청ID */
	String  applyId;
	/** 협약파일그룹ID */
	String  cnvnFileGroupId;
	 /** 협약해지첨부파일그룹ID */
	String  cnvnTrmnatFileGroupId;
	/** 사업계획서ID */
	String bsnsPlanDocId;
	/**접수번호*/
	String receiptNo;
	/**관리자ID*/
	String insiderId;

	/** 사업자인증서세션ID*/
	String bsnmCertSessionId;

	/** 사업단명*/
	String bsnsMgcNm;
	/** 사업단대표자명*/
	String bsnsMgcCeoNm;
	/** 사업단사업자번호*/
	String bsnsMgcBizrno;
	/** 사업단인증서세션ID*/
	String bsnsgCertSessionId;


	/* 이메일*/
	String email;
	/** 휴대폰번호 */
	String mbtlnum;

	/**파일ID */
	String attachmentId;

	private Long beginRowNum;
	private Long itemsPerPage;

}
