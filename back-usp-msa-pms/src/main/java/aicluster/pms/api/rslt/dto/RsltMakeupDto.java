package aicluster.pms.api.rslt.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import lombok.Data;

@Data
public class RsltMakeupDto implements Serializable {
	private static final long serialVersionUID = -6080618745623683811L;
	/** 보완요청일 */
	private String makeupReqDate;
	/** 보완요청내용 */
	private String makeupReqCn;
	/** 첨부파일 목록 */
	List<CmmtAtchmnfl> attachFileList;
}
