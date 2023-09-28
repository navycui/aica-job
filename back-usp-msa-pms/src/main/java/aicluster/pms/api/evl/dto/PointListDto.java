package aicluster.pms.api.evl.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.dto.EvlPointListDto;
import lombok.Data;

@Data
public class PointListDto implements Serializable {
	private static final long serialVersionUID = -1201685549592255171L;
	/** 평가방법코드 (G: EVL_MTHD) */
	private String evlMthdCd;
	/** 접수번호 */
	private String receiptNo;
	/** 회원명 */
	private String memberNm;
	/** 사유 */
	private String adsbtrResnCn;

	List<EvlPointListDto> pointList;
}
