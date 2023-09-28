package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class BsnsPortListDto implements Serializable{
	private static final long serialVersionUID = -3080963101401800015L;
	/** 기업유형 */
	private String memberTypeNm;
	/** 총기업수 */
	private Long taskCnt;
	/** 정부지원금 */
	private Long excutSbsidy;
	/** 자기부담금 */
	private Long excutBsnmAlotm;
	/** 협약 합계 */
	private Long excutCnvnTotamt;
}
