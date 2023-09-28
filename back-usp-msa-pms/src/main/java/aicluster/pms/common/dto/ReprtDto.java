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
public class ReprtDto implements Serializable {
	private static final long serialVersionUID = -5030828784692604423L;
	/** 보고서ID */
	private String reprtId;
	/** 보고서상태코드(G:REPRT_STTUS) */
	private String reprtSttusCd;
	/** 보고서상태 */
	private String reprtSttus;
	/** 보고서 요약내용 */
	private String reprtSumryCn;
	/** 첨부파일그룹ID */
	@JsonIgnore
	private String attachmentGroupId;
	/** 제출일시 */
	@JsonIgnore
	private Date presentnDt;
	/** 사업명 */
	private String bsnsNm;
	/** 사업연도 */
	private String bsnsYear;
	/** 과제명 */
	private String taskNm;
	/** 접수번호 */
	private String receiptNo;
	/** 회원ID */
	@JsonIgnore
	private String memberId;
	/** 사업자명 */
	private String memberNm;
	/** 과제책임자 */
	private String rspnberNm;
	/** 암호화된 휴대폰번호 */
	@JsonIgnore
	private String encMbtlnum;
	/** 신청ID */
	@JsonIgnore
	private String applyId;
	/** 담당자권한코드(G:CHARGER_AUTHOR) */
	@JsonIgnore
	private String chargerAuthorCd;
	/** 첨부파일 목록 */
	List<CmmtAtchmnfl> attachFileList;

	/** 휴대폰번호 */
	public String getMbtlnum() {
		if (string.isBlank(this.encMbtlnum)) {
			return "";
		}
		return aes256.decrypt(this.encMbtlnum, this.applyId);
	}

	public String getPresentnDate() {
		if(this.presentnDt == null) {
			return "";
		}
		return date.format(this.presentnDt, "yyyy-MM-dd");
	}

	public Date getPresentnDt() {
		if (this.presentnDt != null) {
			return new Date(this.presentnDt.getTime());
         }
		return null;
	}
	public void setPresentnDt(Date presentnDt) {
		this.presentnDt = null;
		if (presentnDt != null) {
			this.presentnDt = new Date(presentnDt.getTime());
		}
	}

}
