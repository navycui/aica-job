package aicluster.pms.common.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class StdntMtchListDto implements Serializable {
	private static final long serialVersionUID = 3052685129391926806L;
	/** 신청자 ID */
	@JsonIgnore
	private String applyId;
	/** 최종선정대상ID */
	private String lastSlctnTrgetId;
	/** 처리상태 */
	private String processSttus;
	/** 사업자명 */
	private String memberNm;
	/** 접수번호 */
	private String receiptNo;
	/** 과제명 */
	private String taskNm;
	/** 책임자 */
	private String rspnberNm;
	/** 교육생수 */
	private Long stdntMtchcnt;
	/** 순번 */
	private Long rn;
}
