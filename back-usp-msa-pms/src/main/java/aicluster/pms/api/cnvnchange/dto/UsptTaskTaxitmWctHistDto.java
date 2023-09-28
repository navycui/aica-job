package aicluster.pms.api.cnvnchange.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.pms.common.entity.UsptCnvnChangeReq;
import aicluster.pms.common.entity.UsptTaskTaxitmWctHist;
import lombok.Data;

@Data
public class UsptTaskTaxitmWctHistDto implements Serializable{

	/**
	 * 협약변경관리_과제세목별사업비변경이력
	 */
	private static final long serialVersionUID = 8716786490635930727L;

	/** 협약변경요청 */
	private UsptCnvnChangeReq usptCnvnChangeReq;

	/** 과제세목별사업비변경이력 변경전*/
	List<UsptTaskTaxitmWctHist> usptTaskTaxitmWctHistBeforeList;
	/** 과제세목별사업비변경이력 변경 후*/
	List<UsptTaskTaxitmWctHist> usptTaskTaxitmWctHistAfterList;

	/** 첨부파일 */
	List<CmmtAtchmnfl> attachFileList;
	/** 첨부파일삭제 List */
	List<CmmtAtchmnfl> attachFileDeleteList;
}
