package aicluster.pms.api.cnvnchangehist.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.entity.UsptTaskPrtcmpnyHist;
import aicluster.pms.common.entity.UsptTaskPrtcmpnyInfoHist;
import lombok.Data;

@Data
public class UsptTaskPrtcmpnyInfoChangeHistDto implements Serializable{

	/**
	 * 협약변경내역_참여기업 변경이력
	 */
	private static final long serialVersionUID = 4170091663311017033L;


	/** 과제참여기업정보변경이력 변경전*/
	private UsptTaskPrtcmpnyInfoHist usptTaskPrtcmpnyInfoHistBefore;
	/** 과제참여기업정보변경이력 변경 후*/
	private UsptTaskPrtcmpnyInfoHist usptTaskPrtcmpnyInfoHistAfter;

	/** 과제참여기업변경이력 변경전*/
	List<UsptTaskPrtcmpnyHist> usptTaskPrtcmpnyHistBeforeList;
	/** 과제참여기업변경이력 변경 후*/
	List<UsptTaskPrtcmpnyHist> usptTaskPrtcmpnyHistAfterList;

}
