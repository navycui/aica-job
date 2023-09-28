package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UsptBsnsCnvnHist implements Serializable {
	/**
	 * 사업협약변경이력
	 */
	private static final long serialVersionUID = 1199822255464634052L;
	/*사업협약변경이력ID*/
	String bsnsCnvnHistId;
	 /*협약변경요청ID*/
	String cnvnChangeReqId;
	/*협약변경항목구분코드(G:CHANGE_IEM_DIV)*/
	String changeIemDivCd;
	/*협약변경상태코드(G:CNVN_CHANGE_STTUS)*/
	String cnvnChangeSttusCd;
	 /* 협약변경유형(CNVN_CHANGE_TYPE) */
	String  cnvnChangeTypeCd;
	/*사유내용*/
	String resnCn;
	/*첨부파일그룹ID*/
	String attachmentGroupId;


	/*처리일시*/
	String histDt;
	/* 협약변경상태명 */
	String  cnvnChangeSttusNm;
	/**회원ID*/
	String memberId;
	/**회원명*/
	String memberNm;

	/** 생성자ID */
	@JsonIgnore
	private String creatorId;
	/** 생성일시 */
	@JsonIgnore
	private Date createdDt;

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

}
