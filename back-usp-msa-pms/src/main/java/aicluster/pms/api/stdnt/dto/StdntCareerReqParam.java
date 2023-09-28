package aicluster.pms.api.stdnt.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class StdntCareerReqParam implements Serializable {
	private static final long serialVersionUID = 2853874796823584887L;
	/** 발송방법 */
	private String sndngMth;
	/** 발송내용 */
	private String sndngCn;
	/** 신청ID 목록 */
	List<String> applyIdList;

}
