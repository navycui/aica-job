package aicluster.pms.api.cnvnchange.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.pms.common.entity.UsptCnvnChangeReq;
import aicluster.pms.common.entity.UsptTaskReqstWctHist;
import lombok.Data;

@Data
public class UsptTaskReqstWctHistDto implements Serializable{

	/**
	 *협약변경관리_과제신청사업비변경이력
	 */

	private static final long serialVersionUID = -7124437101724832354L;
	 /** 총사업비 */
	Long totalWct;

	/** 협약변경요청 */
	private UsptCnvnChangeReq usptCnvnChangeReq;
	/** 과제신청사업비변경이력 변경전*/
	List<UsptTaskReqstWctHist> usptTaskReqstWctHistBeforeList;
	/** 과제신청사업비변경이력 변경 후*/
	List<UsptTaskReqstWctHist> usptTaskReqstWctHistAfterList;

	/** 첨부파일 */
	List<CmmtAtchmnfl> attachFileList;
	/** 첨부파일삭제 List */
	List<CmmtAtchmnfl> attachFileDeleteList;
}
