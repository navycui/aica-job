package aicluster.pms.api.cnvncncls.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.pms.common.entity.UsptBsnsCnvn;
import aicluster.pms.common.entity.UsptBsnsCnvnPrtcmpnySign;
import aicluster.pms.common.entity.UsptTaskReqstWct;
import lombok.Data;

@Data
public class CnvnCnclsDto implements Serializable{
	/**
	 *전자협약 관리_admin
	 */
	private static final long serialVersionUID = -9079984700632524247L;

	/** 사업협약 */
	private UsptBsnsCnvn usptBsnsCnvn;
	/** 과제신청사업비 */
	List <UsptTaskReqstWct> usptTaskReqstWct;
	/** 사업협약참여기업서명 */
	List <UsptBsnsCnvnPrtcmpnySign> usptBsnsCnvnPrtcmpnySign;
	/** 첨부파일 */
	List<CmmtAtchmnfl> attachFileList;
	/** 해지첨부파일 */
	List<CmmtAtchmnfl> trmnatAttachFileList;
}
