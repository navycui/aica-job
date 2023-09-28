package aicluster.pms.api.evl.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.entity.UsptEvlIemResultAdsbtr;
import lombok.Data;

@Data
public class PointParam implements Serializable {
	private static final long serialVersionUID = 6187434171054587117L;
	/** 평가구분코드 */
	private String evlDivCd;
	/** 가감사유내용 */
	private String adsbtrResnCn;
	/** 가감목록 */
	List<UsptEvlIemResultAdsbtr> resultList;
}
