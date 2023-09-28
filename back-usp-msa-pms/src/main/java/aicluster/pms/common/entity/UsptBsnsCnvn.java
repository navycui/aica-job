package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UsptBsnsCnvn implements Serializable {
	/**
	 * 사업협약
	 */
	private static final long serialVersionUID = 1340998633457519657L;
	/** 사업협약ID */
	String  bsnsCnvnId;
	/** 신청ID */
	String  applyId;
	/** 협약상태코드(G:CNVN_STTUS) */
	String  cnvnSttusCd;
	/** 협약일자 */
	String  cnvnDe;
	/** 협약시작일 */
	String  cnvnBgnde;
	/** 협약종료일 */
	String  cnvnEndde;
	/** 협약서본문 */
	String  cnvnBdt;
	/** 사업자서명일시 */
	Date  bsnmSignDt;
	/** 사업자인증서세션ID*/
	String bsnmCertSessionId;
	/** 협약해지일 */
	String  cnvnTrmnatDe;
	 /** 협약해지사유구분코드 */
	String  cnvnTrmnatResnCnDivCd;
	/** 협약해지사유구분코드명 */
	String  cnvnTrmnatResnCnDivNm;
	/** 환수대상금액 */
	Long    redempTrgamt;
	/** 환수완료금액 */
	Long    redempComptAmount;
	/** 환수일 */
	String  redempDe;
	/** 협약해지사유내용 */
	String  cnvnTrmnatResnCn;
	 /** 협약파일그룹ID */
	String  cnvnFileGroupId;
	 /** 협약해지첨부파일그룹ID */
	String  cnvnTrmnatFileGroupId;
	/** 사업계획서ID */
	String bsnsPlanDocId;
	 /** 사업선정대상ID */
	String  bsnsSlctnId;

	/** 사업단서명일시 */
	Date    bsnsMgcSignDt;
	/** 사업단서명ID*/
	String bsnsMgcSignId;
	/** 사업단명*/
	String bsnsMgcNm;
	/** 사업단대표자명*/
	String bsnsMgcCeoNm;
	/** 사업단사업자번호*/
	String bsnsMgcBizrno;
	/** char_사업단서명일시 */
	String charBsnsMgcSignDt;
	/** 사업단인증서세션ID*/
	String bsnsgCertSessionId;

	/**사업명*/
	String bsnsNm;
	/**사업년도*/
	String bsnsYear;
	/**접수번호*/
	String receiptNo;
	/** 과제명(국문) */
	String  taskNmKo;
	/** 공고명 */
	String pblancNm;
	/** 협약상태명*/
	String  cnvnSttusNm;
	/** 책임자명*/
	String  rspnberNm;


	/** 신정자ID(주사업자) */
	String memberId;
	/** 신청자명(주사업자) */
	String memberNm;
	/** char_주사업자서명일시 */
	String charBsnmSignDt;
	/** 주사업자 대표자명 */
	String ceoNm;
	/** 주사업자 대표자사업자번호 */
	String bizrno;


	/** I:등록, U:수정, D:삭제 */
	private String flag;
	/** 순번 */
	private int rn;

	Long beginRowNum;
	Long itemsPerPage;

	/** 생성자ID */
	@JsonIgnore
	private String creatorId;
	/** 생성일시 */
	@JsonIgnore
	private Date createdDt;
	/** 수정자ID */
	@JsonIgnore
	private String updaterId;
	/** 수정일시 */
	@JsonIgnore
	private Date updatedDt;

	public Date getCreatedDt() {
		if (this.createdDt != null) {
			return new Date(this.createdDt.getTime());
         }
		return null;
	}
	public void setCreatedDt(Date createdDt) {
		this.createdDt = null;
		if (createdDt != null) {
			this.createdDt = new Date(createdDt.getTime());
		}
	}

	public Date getUpdatedDt() {
		if (this.updatedDt != null) {
			return new Date(this.updatedDt.getTime());
		}
		return null;
	}
	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = null;
		if (updatedDt != null) {
			this.updatedDt = new Date(updatedDt.getTime());
		}
	}

}
