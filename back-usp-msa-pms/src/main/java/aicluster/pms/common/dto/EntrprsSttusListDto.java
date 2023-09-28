package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class EntrprsSttusListDto implements Serializable {
	private static final long serialVersionUID = -5470202017376244263L;
	/** 기업유형 */
	private String memberTypeNm;
	/** 총기업수 */
	private Long totCnt;
	/** 최종선정 건수 */
	private Long lastSlctnCnt;
	/** 사업중 건수 */
	private Long bsnsIng;
	/** 사업종료 건수 */
	private Long bsnsEnd;
}
