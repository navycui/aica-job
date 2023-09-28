package aicluster.pms.api.cnvntrmnat.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.pms.common.entity.UsptBsnsCnvn;
import aicluster.pms.common.entity.UsptBsnsCnvnTrmnatHist;
import lombok.Data;

@Data
public class CnvnTrmnatDto implements Serializable{
	/**
	 *전자협약 해지 관리_admin
	 */
	private static final long serialVersionUID = 6471406674918373702L;

	/** 사업협약 */
	UsptBsnsCnvn usptBsnsCnvn;
	/** 첨부파일 */
	List<CmmtAtchmnfl> attachFileList;
}
