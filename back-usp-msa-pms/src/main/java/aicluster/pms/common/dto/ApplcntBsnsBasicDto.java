package aicluster.pms.common.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class ApplcntBsnsBasicDto implements Serializable {
	private static final long serialVersionUID = 790378502073566565L;
	/** 신청ID */
	private String applyId;
	/** 사업코드 */
	private String bsnsCd;
	/** 사업명 */
	private String bsnsNm;
	/** 사업연도 */
	private String bsnsYear;
	/** 공고명 */
	private String pblancNm;
	/** 과제명 */
	private String taskNm;
	/** 접수번호 */
	private String receiptNo;
	/** 회원명 */
	private String memberNm;
	/** 담당자권한코드 */
	@JsonIgnore
	private String chargerAuthorCd;
	/** 사업시작일 */
	private String bsnsBgnde;
	/** 사업 종료일 */
	private String bsnsEndde;
	/** 사업유형코드 */
	@JsonIgnore
	private String bsnsTypeCd;
	/** 회원ID */
	@JsonIgnore
	private String memberId;
}
