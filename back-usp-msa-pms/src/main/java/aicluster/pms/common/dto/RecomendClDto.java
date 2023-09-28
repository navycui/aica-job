package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class RecomendClDto implements Serializable {

	private static final long serialVersionUID = -1747272983066165313L;

	/** 추천분류유형코드(G:RECOMEND_CL_TYPE) */
	private String recomendClTypeCd;
	/** 추천분류코드(G:RECOMEND_CL) */
	private String recomendClCd;
	/** 추천분류코드명 */
	private String recomendClNm;
	/** 선택여부 */
	private Boolean isChecked;
}
