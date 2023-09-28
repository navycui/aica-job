package aicluster.pms.api.rslt.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import lombok.Data;

@Data
public class RsltMakeupParam implements Serializable {
	private static final long serialVersionUID = -714583683701648961L;
	/** 보완요청내용 */
	private String makeupReqCn;
	/** 삭제 첨부파일 목록 */
	List<CmmtAtchmnfl> deleteFileList;
}
