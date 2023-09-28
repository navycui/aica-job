package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class MtchStdnListDto implements Serializable {
	private static final long serialVersionUID = 2685281608952800096L;
	/** 최종선정대상ID */
	private String lastSlctnTrgetId;
	/** 신청ID */
	private String applyId;
	/** 접수번호 */
	private String receiptNo;
	/** 회원ID */
	private String memberId;
	/** 회원명 */
	private String memberNm;
	/** 희망직무 */
	private String hopeDty;
	/** 최종학교 */
	private String schulNm;
	/** 졸업여부 */
	private String msvcType;
	/** 군필여부 */
	private String grdtnDiv;
	/** 순번 */
	private Long rn;
}
