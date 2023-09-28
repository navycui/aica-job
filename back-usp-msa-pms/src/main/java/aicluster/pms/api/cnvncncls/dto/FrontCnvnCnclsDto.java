package aicluster.pms.api.cnvncncls.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.pms.common.entity.UsptBsnsCnvn;
import aicluster.pms.common.entity.UsptBsnsCnvnPrtcmpnySign;
import aicluster.pms.common.entity.UsptTaskReqstWct;
import lombok.Data;

@Data
public class FrontCnvnCnclsDto implements Serializable{

	/**
	 *전자협약 관리_front
	 */
	private static final long serialVersionUID = 3143747023505605890L;

	/** 사업자번호-인증서처리용*/
	String bizrno;

	/** 사업협약 */
	private UsptBsnsCnvn usptBsnsCnvn;
	/** 과제신청사업비 */
	List <UsptTaskReqstWct> usptTaskReqstWct;
	/** 사업협약참여기업서명 */
	List <UsptBsnsCnvnPrtcmpnySign> usptBsnsCnvnPrtcmpnySign;
	/** 첨부파일 */
	List<CmmtAtchmnfl> attachFileList;


}
