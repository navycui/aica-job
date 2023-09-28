package aicluster.pms.api.rslt.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class PresentnReqParam implements Serializable {
	private static final long serialVersionUID = -5734691472885332129L;
	/** 발송방법 */
	private String sndngMth;
	/** 발송내용 */
	private String sndngCn;
	/** 신청ID 목록 */
	List<String> applyIdList;

}
