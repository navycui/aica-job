package aicluster.pms.api.cnvnchangehist.dto;

import java.io.Serializable;

import aicluster.pms.common.entity.UsptCnvnApplcntHist;
import lombok.Data;

@Data
public class UsptCnvnApplcntChangeHistDto implements Serializable{

	/**
	 * 협약변경내역_협약신청자정보변경이력
	 */
	private static final long serialVersionUID = -6903637634582946745L;

	/** 협약신청자정보변경이력 변경전*/
	private UsptCnvnApplcntHist usptCnvnApplcntHistBefore;
	/** 협약신청자정보변경이력 변경 후*/
	private UsptCnvnApplcntHist usptCnvnApplcntHistAfter;
}
