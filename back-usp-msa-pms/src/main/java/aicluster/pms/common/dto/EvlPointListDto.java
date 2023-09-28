package aicluster.pms.common.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class EvlPointListDto implements Serializable {
	private static final long serialVersionUID = 6845710565006303807L;
	/** 평가방법코드 (G: EVL_MTHD) */
	@JsonIgnore
	private String evlMthdCd;
	/** 평가항목ID */
	private String evlIemId;
	/** 평가항목명 */
	private String evlIemNm;
	/** 평가항목내용 */
	private String evlIemCn;
	/** 평가항목결과가감ID */
	private String evlIemResultAdsbtrId;
	/** 평가항목ID */
	private Integer allotScore;
	/** 평가항목ID */
	private Integer evlScore;
	/** 평가내용 */
	private String evlCn;
	/** 평가항목ID */
	@JsonIgnore
	private String adsbtrResnCn;

}
