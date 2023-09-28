package aicluster.pms.api.cnvnchangehist.dto;

import java.io.Serializable;

import aicluster.pms.common.entity.UsptTaskRspnberHist;
import lombok.Data;

@Data
public class UsptTaskRspnberChangeHistDto implements Serializable{

	/**
	 * 협약변경내역_과제책임자변경이력
	 */
	private static final long serialVersionUID = 3638389940044223969L;

	/** 과제책임자변경이력 변경전*/
	private UsptTaskRspnberHist usptTaskRspnberHistBefore;
	/** 과제책임자변경이력 변경 후*/
	private UsptTaskRspnberHist usptTaskRspnberHistAfter;
}
