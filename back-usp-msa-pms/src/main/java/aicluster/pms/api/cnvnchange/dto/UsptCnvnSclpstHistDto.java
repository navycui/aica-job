package aicluster.pms.api.cnvnchange.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.pms.common.entity.UsptBsnsPlanApplcnt;
import aicluster.pms.common.entity.UsptCnvnChangeReq;
import aicluster.pms.common.entity.UsptCnvnSclpstHist;
import lombok.Data;

@Data
public class UsptCnvnSclpstHistDto implements Serializable{

	/**
	 *협약변경관리_수행기관신분
	 */
	private static final long serialVersionUID = -635863279568217764L;

	/** 협약변경요청 */
	private UsptCnvnChangeReq usptCnvnChangeReq;
	/** 사업계획신청자  변경전*/
	private UsptBsnsPlanApplcnt usptBsnsPlanApplcntBefore;
	/** 협약수행기관신분변경이력 변경 후*/
	private UsptCnvnSclpstHist usptCnvnSclpstHistAfter;

	/** 첨부파일 */
	List<CmmtAtchmnfl> attachFileList;
	/** 첨부파일삭제 List */
	List<CmmtAtchmnfl> attachFileDeleteList;
}
