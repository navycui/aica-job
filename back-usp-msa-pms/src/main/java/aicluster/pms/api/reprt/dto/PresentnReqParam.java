package aicluster.pms.api.reprt.dto;

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
	/** 최종선정대상ID 목록 */
	List<String> trgetIdList;

}
