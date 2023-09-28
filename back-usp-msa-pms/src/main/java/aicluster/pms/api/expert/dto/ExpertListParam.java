package aicluster.pms.api.expert.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class ExpertListParam implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 7243621021634324369L;
	/** 전문가신청처리상태코드(G:EXPERT_REQST_PROCESS_STTUS) */
	String expertReqstProcessSttusCd;
	/**신청일시작**/
	String reqstDayStart;
	/**신청일종료**/
	String reqstDayEnd;
	/** 전문가명 */
	String expertNm;
	/** 직장명 */
	String wrcNm;
	/** 전문가ID */
	String  expertId;
	/** 암호화된 생년월일 */
	String  encBrthdy;
	/** 암호화된 휴대폰번호 */
	String  encMbtlnum;
	/** 암호화된 이메일 */
	String  encEmail;
	 /** 사유내용 */
	String  resnCn;
	 /** 첨부파일그룹ID */
	String  attachmentGroupId;

	/** 전문가분류ID */
	String[] expertClId;

	/** 신청조회여부 */
	String  expertReqstYn;
	/** 담당자ID */
	String  mberId;

	/** 엑셀 다운로드 여부 */
	private Boolean isExcel;

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
