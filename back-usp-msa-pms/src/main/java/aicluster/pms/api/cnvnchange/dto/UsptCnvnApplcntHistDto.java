package aicluster.pms.api.cnvnchange.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.pms.common.entity.UsptCnvnApplcntHist;
import aicluster.pms.common.entity.UsptCnvnChangeReq;
import lombok.Data;

@Data
public class UsptCnvnApplcntHistDto implements Serializable{

	/**
	 * 협약변경관리_협약신청자정보변경이력
	 */
	private static final long serialVersionUID = -8928706234270556190L;

	/** 협약변경요청 */
	private UsptCnvnChangeReq usptCnvnChangeReq;
	/** 협약신청자정보변경이력 변경전*/
	private UsptCnvnApplcntHist usptCnvnApplcntHistBefore;
	/** 협약신청자정보변경이력 변경 후*/
	private UsptCnvnApplcntHist usptCnvnApplcntHistAfter;

	/** 첨부파일 */
	List<CmmtAtchmnfl> attachFileList;
	/** 첨부파일삭제 List */
	List<CmmtAtchmnfl> attachFileDeleteList;
}
