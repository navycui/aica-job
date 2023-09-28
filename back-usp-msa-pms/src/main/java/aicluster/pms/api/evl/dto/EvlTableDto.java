package aicluster.pms.api.evl.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.dto.EvlTableListDto;
import lombok.Data;

@Data
public class EvlTableDto implements Serializable {
	private static final long serialVersionUID = 4840086991672349376L;
	/** 평가방법코드 (G: EVL_MTHD) */
	private String evlMthdCd;
	/** 평가위원명 */
	private String evlMfcmmNm;
	/** 접수번호 */
	private String receiptNo;
	/** 사용자명 */
	private String memberNm;
	/** 평가의견 */
	private String evlOpinion;
	/** 평가결과ID */
	private String  evlResultId;
	/** 위원장여부 */
	private Boolean charmn;
	/** 평가목록 */
	List<EvlTableListDto> list;
}
