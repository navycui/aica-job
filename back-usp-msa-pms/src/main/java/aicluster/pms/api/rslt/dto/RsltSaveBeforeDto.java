package aicluster.pms.api.rslt.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.dto.ApplcntBsnsBasicDto;
import aicluster.pms.common.entity.UsptBsnsRsltIdx;
import lombok.Data;

@Data
public class RsltSaveBeforeDto implements Serializable {
	private static final long serialVersionUID = 238184654714258706L;
	/** 기본정보 */
	private ApplcntBsnsBasicDto basicInfo;
	/** 성과지표 목록 */
	List<UsptBsnsRsltIdx> rsltIdxList;
}
