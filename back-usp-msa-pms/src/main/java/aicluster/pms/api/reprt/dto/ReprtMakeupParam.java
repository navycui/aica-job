package aicluster.pms.api.reprt.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import lombok.Data;

@Data
public class ReprtMakeupParam implements Serializable {
	private static final long serialVersionUID = 2110434030234366568L;
	/** 보완요청내용 */
	private String makeupReqCn;
	/** 삭제 첨부파일 목록 */
	List<CmmtAtchmnfl> deleteFileList;
}
