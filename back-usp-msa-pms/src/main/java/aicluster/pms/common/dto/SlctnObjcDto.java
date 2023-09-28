package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import lombok.Data;

@Data
public class SlctnObjcDto implements Serializable {
	private static final long serialVersionUID = -7025542796834260388L;
	/** 이의신청ID */
	private String objcReqstId;
	/** 이의처리상태코드 (G:LAST_SLCTN_OBJC_PROCESS_STTUS) */
	private String lastSlctnObjcProcessSttusCd;
	/** 이의처리상태 */
	private String lastSlctnObjcProcessSttus;
	/** 이의신청내용 */
	private String objcReqstCn;
	/** 심의일 */
	private String dlbrtDe;
	/** 심의내용 */
	private String dlbrtCn;
	/** 담당부서 */
	private String deptNm;
	/** 담당자 */
	private String chargerNm;
	/** 접수일 */
	private String rceptDate;

	/** 신청자첨부파일그룹ID */
	@JsonIgnore
	private String applcntAttachmentGroupId;
	/** 심의결과첨부파일그룹ID */
	@JsonIgnore
	private String dlbrtAttachmentGroupId;

	/** 사업명 */
	private String bsnsNm;
	/** 사업연도 */
	private String bsnsYear;
	/** 공고명 */
	private String pblancNm;
	/** 공고번호 */
	private String pblancNo;
	/** 모집유형 */
	private String ordtmRcrit;
	/** 접수차수 */
	private String rceptOdr;
	/** 접수번호 */
	private String receiptNo;
	/** 회원명 */
	private String memberNm;
	/** 선정여부 */
	private String slctn;

	/** 선정형식
	 * LAST:최종선정, EVL:단계선정
	 * */
	@JsonIgnore
	private String slctnType;

	/** 담당자권한코드 */
	@JsonIgnore
	private String chargerAuthorCd;

	/** 신청자 첨부파일 */
	List<CmmtAtchmnfl> applcntAttachmentFileList;
	/** 심의결과 첨부파일 */
	List<CmmtAtchmnfl> dlbrtAttachmentFileList;

	/** 회원ID */
	@JsonIgnore
	private String memberId;
	/** 연락처 */
	@JsonIgnore
	private String encMobileNo;
	public String getMobileNo() {
		if (string.isBlank(this.encMobileNo)) {
			return null;
		}
		String mobileNo = aes256.decrypt(this.encMobileNo, this.memberId);
		return mobileNo;
	}



	/** 이의신청일시 */
	@JsonIgnore
	private Date objcReqstDt;
	public String getObjcReqstDate() {
		if(this.objcReqstDt == null) {
			return "";
		} else {
			return date.format(this.objcReqstDt, "yyyy-MM-dd HH:mm");
		}
	}

	public Date getObjcReqstDt() {
		if (this.objcReqstDt != null) {
			return new Date(this.objcReqstDt.getTime());
         }
		return null;
	}
	public void setObjcReqstDt(Date objcReqstDt) {
		this.objcReqstDt = null;
		if (objcReqstDt != null) {
			this.objcReqstDt = new Date(objcReqstDt.getTime());
		}
	}
}
