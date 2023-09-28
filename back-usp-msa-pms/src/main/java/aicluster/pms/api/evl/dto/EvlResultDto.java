package aicluster.pms.api.evl.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.pms.common.dto.EvlResultListDto;
import lombok.Data;

@Data
public class EvlResultDto implements Serializable {
	private static final long serialVersionUID = -4270381760084226632L;
	/** 평가결과 목록 */
	List<EvlResultListDto> resultList;
	/** 첨부파일 목록 */
	List<CmmtAtchmnfl> attachmentList;
}
