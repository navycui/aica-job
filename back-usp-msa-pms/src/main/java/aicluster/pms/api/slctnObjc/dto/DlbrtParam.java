package aicluster.pms.api.slctnObjc.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import lombok.Data;

@Data
public class DlbrtParam implements Serializable {
	private static final long serialVersionUID = -4573651324185061047L;
	/** 심의내용 */
	private String dlbrtCn;
	/** 심의일 */
	private String dlbrtDate;
	/** 삭제 파일 목록 */
	List<CmmtAtchmnfl> deleteFileList;
}
