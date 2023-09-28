package aicluster.pms.api.bsnsplan.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.pms.common.entity.UsptBsnsApplyRealm;
import aicluster.pms.common.entity.UsptBsnsPlanDoc;
import aicluster.pms.common.entity.UsptTaskPartcpts;
import aicluster.pms.common.entity.UsptTaskPrtcmpny;
import aicluster.pms.common.entity.UsptTaskReqstWct;
import aicluster.pms.common.entity.UsptTaskRspnber;
import lombok.Data;

@Data
public class FrontBsnsPlanDto implements Serializable{

	/**
	 *사업계획서 접수관리_front
	 */
	private static final long serialVersionUID = -1677658213874094984L;

	String successYn;   /**성공여부*/
	/** 사업계획서 */
	private UsptBsnsPlanDoc usptBsnsPlanDoc;
	/** 과제책임자 */
	private UsptTaskRspnber usptTaskRspnber;
	/** 참여기업 */
	List <UsptTaskPrtcmpny> usptTaskPrtcmpny;
	/** 과제참여자 */
	List <UsptTaskPartcpts> usptTaskPartcpts;
	/** 과제신청사업비 */
	List <UsptTaskReqstWct> usptTaskReqstWct;
	/** 첨부파일 */
	List<CmmtAtchmnfl> attachFileList;
	/** 첨부파일삭제 List */
	List<CmmtAtchmnfl> attachFileDeleteList;
	/** 지원분야(과제분야) 목록 */
	List<UsptBsnsApplyRealm> applyRealmList;
}
