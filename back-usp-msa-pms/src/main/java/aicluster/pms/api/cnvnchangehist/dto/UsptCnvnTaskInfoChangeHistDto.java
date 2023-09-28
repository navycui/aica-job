package aicluster.pms.api.cnvnchangehist.dto;

import java.io.Serializable;

import aicluster.pms.common.entity.UsptCnvnTaskInfoHist;
import lombok.Data;

@Data
public class UsptCnvnTaskInfoChangeHistDto implements Serializable{

	/**
	 * 협약변경내역_협약과제정보변경이력
	 */
	private static final long serialVersionUID = 2200837273042745171L;

	/** 협약과제정보변경이력 변경전*/
	private UsptCnvnTaskInfoHist usptCnvnTaskInfoHistBefore;
	/** 협약과제정보변경이력 변경 후*/
	private UsptCnvnTaskInfoHist usptCnvnTaskInfoHistAfter;

}
