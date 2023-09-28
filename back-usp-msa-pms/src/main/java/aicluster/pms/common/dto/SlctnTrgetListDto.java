package aicluster.pms.common.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class SlctnTrgetListDto implements Serializable {
	private static final long serialVersionUID = -9194405828799562648L;
	/** 신청ID */
	private String applyId;
	/** 접수번호 */
	private String receiptNo;
	/** 회원ID */
	private String memberId;
	/** 사업자명 */
	private String memberNm;
	/** 선정여부 */
	private Boolean slctn;
	/** 선정여부(한글) */
	@JsonIgnore
	private String slctnAt;
	/** 최종선정대상ID */
	private String lastSlctnTrgetId;
	/** 사업선정대상ID */
	private String bsnsSlctnId;
	/** 총사업비 */
	private Long totalWct;
	/** 지원예산 */
	private Long sportBudget;
	/** 부담금현금 */
	private Long alotmCash;
	/** 부담금현물 */
	private Long alotmActhng;
	/** 순번 */
	private Long rn;
	/** 사업시작일 */
	private String bsnsBgnde;
	/** 사업종료일 */
	private String bsnsEndde;
}
