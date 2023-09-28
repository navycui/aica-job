package aicluster.pms.api.rslt.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.entity.UsptRsltIdxIemCn;
import lombok.Data;

@Data
public class RsltIdxParam implements Serializable {
	private static final long serialVersionUID = 6737750965732147655L;
	/** 성과지표항목ID */
	private String rsltIdxIemId;
	/** 성과지표ID */
	private String rsltIdxId;
	/** 첨부파일 순서 */
	private Integer attachFileOrder;
	/** 성과지표항목내용 목록 */
	List<UsptRsltIdxIemCn> rsltIdxIemCnList;
	/** 삭제 첨부파일 ID */
	private String deleteAttachmentId;
}
