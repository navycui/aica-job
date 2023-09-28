package aicluster.pms.api.selection.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.dto.SlctnDetailDto;
import aicluster.pms.common.dto.SlctnTrgetListDto;
import lombok.Data;

@Data
public class SelectionDto implements Serializable {
	private static final long serialVersionUID = -5111078869094013953L;
	/** 선정정보 */
	private SlctnDetailDto slctnInfo;
	/** 선정대상목록 */
	List<SlctnTrgetListDto> slctnTrgetList;
}
