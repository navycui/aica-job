package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class InfoNtcnDto implements Serializable {
	private static final long serialVersionUID = 7180121414646398715L;
	/** 추천분류유형코드 */
	private String recomendClTypeCd;
	/** 추천분류유형명 */
	private String recomendClTypeNm;
	/** 추천분류코드 */
	private String recomendClCd;
	/** 추천분류코드명 */
	private String recomendClNm;
	/** 선택여부 */
	private Boolean isCheck;
}
