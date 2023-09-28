package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.Data;

@Data
public class UsptCnvnChangeReq implements Serializable {
	/**
	 * 협약변경요청
	 */
	private static final long serialVersionUID = 1145448979054116937L;

	/** 협약변경요청ID */
	String  cnvnChangeReqId;
	/** 사업협약ID */
	String  bsnsCnvnId;
	 /** 협약변경항목구분코드(G:CHANGE_IEM_DIV) */
	String  changeIemDivCd;
	 /** 협약변경상태코드(G:CNVN_CHANGE_STTUS) */
	String  cnvnChangeSttusCd;
	 /** 협약변경유형(CNVN_CHANGE_TYPE) */
	String  cnvnChangeTypeCd;
	/** 사유내용 */
	String  resnCn;

	/**접수번호*/
	String receiptNo;
	/** 과제명(국문) */
	String taskNmKo;
	 /** 협약변경항목구분명 */
	String  changeIemDivNm;
	 /** 협약변경상태명 */
	String  cnvnChangeSttusNm;
	 /** 협약변경유형명 */
	String  cnvnChangeTypeNm;
	 /** 신청일 */
	String  reqDe;
	/**회원ID*/
	String memberId;
	/**회원명*/
	String memberNm;
	/**사업계획서ID*/
	String bsnsPlanDocId;
	 /** 사업선정대상ID */
	String  bsnsSlctnId;
	/** 사업시작일  */
	String bsnsBgnde;
	/** 사업종료일  */
	String bsnsEndde;
	/** 과제명(영문) */
	String  taskNmEn;
	 /** 지원분야 */
	String  applyField;
	 /** 지원분야명 */
	String  applyFieNm;
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
	/** 사업기간 */
	String bsnsPd = "";
	/** 사업기간(전체) */
	String bsnsPdAll = "";
	/** 사업기간(당해) */
	String bsnsPdYw = "";
	/**사업년도**/
	String bsnsYear;

	/** 과제책임자ID */
	String  taskRspnberId;
	/*과제책임자명*/
	String  rspnberNm;
	/*과제책임자명_휴대폰*/
	String  encMbtlnum;
	/*과제책임자명_이메일*/
	String  encEmail;

	/*첨부파일그룹ID*/
	String attachmentGroupId;
	/**신청자ID**/
	String applcntId;

	/** 생성자ID */
	String creatorId;
	/** 생성일시 */
	Date createdDt;

	/**
	 * 복호화된 휴대폰번호
	 *
	 * @return
	 */
	public String getMbtlnum() {
		if (string.isBlank(this.encMbtlnum)) {
			return null;
		}
		return aes256.decrypt(this.encMbtlnum, this.taskRspnberId);
	}
	/**
	 * 복호화된 이메일
	 *
	 * @return
	 */
	public String getEmail() {
		if (string.isBlank(this.encEmail)) {
			return null;
		}
		return aes256.decrypt(this.encEmail, this.taskRspnberId);
	}
}
