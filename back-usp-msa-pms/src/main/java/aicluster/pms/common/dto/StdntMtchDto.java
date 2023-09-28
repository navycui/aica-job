package aicluster.pms.common.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import aicluster.pms.common.entity.UsptBsnsPblancApplcntEnt;
import lombok.Data;

@Data
public class StdntMtchDto implements Serializable {
	private static final long serialVersionUID = 7925351611051218849L;
	/** 신청자 ID */
	@JsonIgnore
	private String applyId;
	/** 처리상태 */
	private String processSttus;
	/** 사업자명 */
	private String memberNm;
	/** 회원ID */
	@JsonIgnore
	private String memberId;
	/** 접수번호 */
	private String receiptNo;
	/** 사업자번호 */
	private String bizrno;
	/** 과제명(국문) */
	private String taskNmKo;
	/** 과제명(영문) */
	private String taskNmEn;
	/** 지원분야 */
	private String applyRealmNm;
	/** 사업기간 */
	private String bsnsPd = "";
	/** 사업기간(전체) */
	private String bsnsPdAll = "";
	/** 사업기간(당해) */
	private String bsnsPdYw = "";
	/** 담당자권한코드 */
	@JsonIgnore
	private String chargerAuthorCd;
	/** 사업시작일 */
	@JsonIgnore
	private String bsnsBgnde;
	/** 사업종료일 */
	@JsonIgnore
	private String bsnsEndde;

	/** 기업정보 */
	UsptBsnsPblancApplcntEnt applcntEnt;
}
