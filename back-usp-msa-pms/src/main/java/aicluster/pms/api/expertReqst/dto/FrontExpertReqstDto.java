package aicluster.pms.api.expertReqst.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.pms.common.entity.UsptExpert;
import aicluster.pms.common.entity.UsptExpertAcdmcr;
import aicluster.pms.common.entity.UsptExpertCareer;
import aicluster.pms.common.entity.UsptExpertClMapng;
import aicluster.pms.common.entity.UsptExpertCrqfc;
import lombok.Data;

@Data
public class FrontExpertReqstDto implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = -7332892624338263902L;
	/** 약관동의 세션ID */
	private String sessionId;
	/** 신청자 전문가정보 */
	private UsptExpert usptExpert;
	/** 전문가 분야정보 */
	List<UsptExpertClMapng> usptExpertClMapng;
	/** 전문가 경력정보 */
	List<UsptExpertCareer> usptExpertCareer;
	/** 전문가 학력정보 */
	List<UsptExpertAcdmcr> usptExpertAcdmcr;
	/** 전문가 자격증정보 */
	List<UsptExpertCrqfc> usptExpertCrqfc;
	/** 첨부파일 */
	List<CmmtAtchmnfl> attachFileList;
	/** 첨부파일삭제 List */
	List<CmmtAtchmnfl> attachFileDeleteList;
}
