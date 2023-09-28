package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class EvlApplcntListDto implements Serializable {
	private static final long serialVersionUID = -3782394300015925138L;
	/** 신청ID */
	private String applyId;
	/** 접수번호 */
	private String receiptNo;
	/** 신청자 */
	private String memberNm;
	/** 과제 담당자 */
	private String rspnberNm;
}
