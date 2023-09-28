package aicluster.pms.api.bsnsplan.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class BsnsPlanParam implements Serializable{
	/**
	 * 사업계획서 접수관리_admin
	 */
	private static final long serialVersionUID = 9020980875574216347L;
	/**제출일시작**/
	String presentnDtStart;
	/**제출일종료**/
	String presentnDtEnd;
	/** 사업계획제출상태코드(G:PLAN_PRESENTN_STTUS) */
	String planPresentnSttusCd;
	/** 과제명(국문) */
	String taskNmKo;
	/** 공고명 */
	String pblancNm;
	 /** 사업계획서ID */
	String  bsnsPlanDocId;
	/** 사업선정대상ID */
	String  bsnsSlctnId;
	/** 과제신청사업비ID */
	String  taskReqstWctId;
	/** 보완요청파일그룹ID */
	String  makeupReqFileGroupId;
	/** 접수번호(BA + 8자리 순번) */
	String receiptNo;
	/** 제출첨부파일그룹ID */
	String  prsentrAttachmentGroupId;
	/**사업년도*/
	String bsnsYear;
	/**사업명*/
	String bsnsNm;
	/** 책임자명_회원명 */
	String rspnberNm;
	/** 신청ID */
	String applyId;
	/** 신정자ID */
	String memberId;
	/** 신청자명 */
	String memberNm;
	/** 휴대폰번호 */
	String mbtlnum;
	/* 이메일*/
	String email;
	/**파일ID */
	String attachmentId;
	/**관리자ID*/
	String insiderId;


	private Long beginRowNum;
	private Long itemsPerPage;

	@JsonIgnore
	String  updaterId;              /** 수정자ID(CMMT_MEMBER.MEMBER_ID) */
	@JsonIgnore
	private Date updatedDt;         /** 수정일시 */

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
