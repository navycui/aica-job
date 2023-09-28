package aicluster.pms.api.cnvnchangehist.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.entity.UsptTaskPartcptsHist;
import lombok.Data;

@Data
public class UsptTaskPartcptsChangeHistDto implements Serializable{

	/**
	 *협약변경내역_과제참여자변경이력
	 */
	private static final long serialVersionUID = 4499355988627981299L;

	/** 과제참여자변경이력 변경전*/
	List<UsptTaskPartcptsHist> usptTaskPartcptsHistBeforeList;
	/** 과제참여자변경이력 변경 후*/
	List<UsptTaskPartcptsHist> usptTaskPartcptsHistAfterList;
}
