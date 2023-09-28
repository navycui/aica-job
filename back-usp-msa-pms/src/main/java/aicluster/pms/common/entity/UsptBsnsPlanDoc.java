package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UsptBsnsPlanDoc implements Serializable {

	/**
	 * 사업계획서
	 */
	private static final long serialVersionUID = 3062981210743390078L;
	 /** 사업계획서ID */
	String  bsnsPlanDocId;
	 /** 사업선정대상ID */
	String  bsnsSlctnId;
	/** 과제명(국문) */
	String  taskNmKo;
	 /** 과제명(영문) */
	String  taskNmEn;
	 /** 지원분야 */
	String  applyField;
	 /** 지원분야명 */
	String  applyFieNm;
	 /** 제출자ID */
	String  prsentrId;
	 /** 제출일시 */
	Date  presentnDt;
	 /** 제출첨부파일그룹ID */
	String  prsentrAttachmentGroupId;
	/** 보완요청내용 */
	String  makeupReqCn;
	/** 보완요청자ID */
	String  makeupRqesterId;
	/** 보완요청일시 */
	Date    makeupReqDt;
	 /** 보완요청파일그룹ID */
	String  makeupReqFileGroupId;
	/** 사업계획제출상태코드(G:PLAN_PRESENTN_STTUS) */
	String  planPresentnSttusCd;
	/** 사업계획제출상태코드명 */
	String planPresentnSttusNm;
	/** 참여기업수 */
	Long    partcptnCompanyCnt;
	 /** 중소기업수 */
	Long    smlpzCnt;
	 /** 중견기업수 */
	Long    mspzCnt;
	 /** 기타수 */
	Long    etcCnt;
	 /** 총사업비 */
	Long    totalWct;

	/**사업명*/
	String bsnsNm;
	/**사업년도*/
	String bsnsYear;
	/**컨소시엄여부*/
	Boolean cnsrtm;
	/**접수번호*/
	String receiptNo;
	/**제출일시작**/
	String presentnDtStart;
	/**제출일종료**/
	String presentnDtEnd;
	/** 공고명 */
	String pblancNm;
	/** 사업시작일  */
	String bsnsBgnde;
	/** 사업종료일  */
	String bsnsEndde;
	/** 책임자명 */
	String rspnberNm;
	/** 제출일*/
	String presentnDy;
	/** 신청ID */
	String applyId;

	/** 사업기간 */
	String bsnsPd = "";
	/** 사업기간(전체) */
	String bsnsPdAll = "";
	/** 사업기간(당해) */
	String bsnsPdYw = "";
	/**첨부파일ID */
	String attachmentId;

	/** 신청자구분 */
	String memberType;
	/** 신정자ID */
	String memberId;
	/** 신청자명 */
	String memberNm;
	/** 휴대폰번호 */
	String mbtlnum;
	/** 생년월일 */
	String brthdy;
	/* 이메일*/
	String email;
	/*성별*/
	String gender;
	/*담당자명*/
	String chargerNm;
	/*대표자명*/
	String ceoNm;
	/*사업자등록번호*/
	String bizrno;
	/** 사업코드 */
	String bsnsCd;

	/** 발송방법 */
	private String sndngMth;
	/** 순번 */
	private int rn;
	/** 요청일시 */
	String requestDt;

	Long beginRowNum;
	Long itemsPerPage;

	@JsonIgnore
	private String creatorId;		/** 생성자 ID */
	@JsonIgnore
	private Date createdDt;			/** 생성일시 */
	@JsonIgnore
	String  updaterId;              /** 수정자ID(CMMT_MEMBER.MEMBER_ID) */
	@JsonIgnore
	private Date updatedDt;         /** 수정일시 */

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
