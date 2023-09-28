package aicluster.pms.common.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class EvlTableListDto implements Serializable {
	private static final long serialVersionUID = 5180707009025679472L;
	/** 평가방법코드 (G: EVL_MTHD) */
	@JsonIgnore
	private String evlMthdCd;
	/** 평가항목명 */
	private String evlIemNm;
	/** 평가항목내용 */
	private String evlIemCn;
	/** 배점점수 */
	private Integer allotScore;
	/** 평가점수 */
	private Integer evlScore;
	/** 평가내용 */
	private String evlCn;
	/** 평가의견 */
	@JsonIgnore
	private String evlOpinion;
	/** 평가항목결과ID */
	private String  evlIemResultId;
	/** 평가결과ID */
	private String  evlResultId;
	/** 평가항목ID */
	private String  evlIemId;
}
