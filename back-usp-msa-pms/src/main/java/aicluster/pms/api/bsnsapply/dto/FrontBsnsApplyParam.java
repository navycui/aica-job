package aicluster.pms.api.bsnsapply.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.entity.UsptBsnsPblancApplcntEnt;
import aicluster.pms.common.entity.UsptBsnsPblancApplyAttach;
import aicluster.pms.common.entity.UsptBsnsPblancApplyChklst;
import aicluster.pms.common.entity.UsptBsnsPblancApplyTask;
import aicluster.pms.common.entity.UsptBsnsPblancApplyTaskPartcpts;
import lombok.Data;

@Data
public class FrontBsnsApplyParam implements Serializable{
	private static final long serialVersionUID = 1701685169167217416L;
	/** 약관동의 세션ID */
	private String sessionId;
	/** 신청자 기업정보 */
	private UsptBsnsPblancApplcntEnt applcntEnt;
	/** 신청 과제정보 */
	private UsptBsnsPblancApplyTask applyTask;
	/** 체크리스트 */
	List<UsptBsnsPblancApplyChklst> chkList;
	/** 과제 참여인력 */
	List<UsptBsnsPblancApplyTaskPartcpts> taskPartcptsList;
	/** 파일 업로드 목록 */
	List<UsptBsnsPblancApplyAttach> uploadFileList;
}
