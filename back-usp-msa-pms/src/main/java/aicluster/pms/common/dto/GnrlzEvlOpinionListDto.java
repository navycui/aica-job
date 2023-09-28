package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class GnrlzEvlOpinionListDto implements Serializable {
	private static final long serialVersionUID = -5786721235042707781L;
	/** 평가위원명 */
	private String evlMfcmmNm;
	/** 평가내용 */
	private String evlCn;
}
