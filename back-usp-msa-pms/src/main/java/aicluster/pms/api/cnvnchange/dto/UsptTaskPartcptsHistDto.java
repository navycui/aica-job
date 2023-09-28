package aicluster.pms.api.cnvnchange.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.pms.common.entity.UsptCnvnChangeReq;
import aicluster.pms.common.entity.UsptTaskPartcptsHist;
import lombok.Data;

@Data
public class UsptTaskPartcptsHistDto implements Serializable{

	/**
	 *협약변경관리_과제참여자변경이력
	 */
	private static final long serialVersionUID = 2224880155652927535L;

	/** 협약변경요청 */
	private UsptCnvnChangeReq usptCnvnChangeReq;
	/** 과제참여자변경이력 변경전*/
	List<UsptTaskPartcptsHist> usptTaskPartcptsHistBeforeList;
	/** 과제참여자변경이력 변경 후*/
	List<UsptTaskPartcptsHist> usptTaskPartcptsHistAfterList;

	/** 첨부파일 */
	List<CmmtAtchmnfl> attachFileList;
	/** 첨부파일삭제 List */
	List<CmmtAtchmnfl> attachFileDeleteList;
}
