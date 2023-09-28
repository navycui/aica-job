package aicluster.pms.api.bsns.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.dto.RecomendClDto;
import aicluster.pms.common.entity.UsptStdrBsns;
import aicluster.pms.common.entity.UsptStdrCharger;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StdrBsnsBasicDto extends UsptStdrBsns implements Serializable {
	private static final long serialVersionUID = -1782515609110703915L;
	/** 사업분류명 */
	private String bsnsClNm;
	/** 추천분류 */
	List<RecomendClDto> stdrRecomendClList;
	/** 담당자 */
	List<UsptStdrCharger> stdrChargerList;
}
