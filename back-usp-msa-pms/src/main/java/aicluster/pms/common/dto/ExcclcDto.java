package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import lombok.Data;

@Data
public class ExcclcDto implements Serializable {
	private static final long serialVersionUID = 5907218568723110954L;
	/** 사업협약ID */
	private String bsnsCnvnId;
	/** 사업명 */
	private String bsnsNm;
	/** 회원ID */
	@JsonIgnore
	private String memberId;
	/** 회원명 */
	private String memberNm;
	/** 사업자등록번호 */
	private String bizrno;
	/** 대표자명 */
	private String ceoNm;
	/** 담당자명 */
	private String chargerNm;
	/** 과제명 */
	private String taskNm;
	/** 접수번호 */
	private String receiptNo;
	/** 협약상태코드(G:CNVN_STTUS) */
	private String cnvnSttusCd;
	/** 협약상태*/
	private String cnvnSttus;
	/** 사업연도 */
	private String bsnsYear;
	/** 협약내역 보조금 */
	private Long sportBudget;
	/** 협약내역 부담금 */
	private Long alotm;
	/** 협약내역 협약총액 */
	private Long wctTotamt;
	/** 신청예산 산업연도 */
	private String reqstBsnsYear;
	/** 과제신청사업비ID */
	private String taskReqstWctId;
	/** 집행 보조금 */
	private Long excutSbsidy;
	/** 집행 사업자 부담금 */
	private Long excutBsnmAlotm;
	/** 집행 협약총액 */
	private Long excutCnvnTotamt;
	/** 사업담당자 권한코드 */
	private String chargerAuthorCd;
	/** 첨부파일 목록 */
	List<CmmtAtchmnfl> fileList;
	/** 첨부파일그룹ID */
	@JsonIgnore
	private String attachmentGroupId;
	/** 신청ID */
	@JsonIgnore
	private String applyId;
	/** 암호화된 모바일번호 */
	@JsonIgnore
	private String encMobileNo;
	public String getMbtlnum() {
		if (string.isBlank(this.encMobileNo)) {
			return "";
		}
		return aes256.decrypt(this.encMobileNo, this.applyId);
	}

	/** 협약시작일 */
	@JsonIgnore
	private String cnvnBgnde;
	/** 협약종료일 */
	@JsonIgnore
	private String cnvnEndde;

	public String getCnvnBgnDate() {
		return date.format(string.toDate(this.cnvnBgnde), "yyyy-MM-dd");
	}
	public String getCnvnEndDate() {
		return date.format(string.toDate(this.cnvnEndde), "yyyy-MM-dd");
	}

}
