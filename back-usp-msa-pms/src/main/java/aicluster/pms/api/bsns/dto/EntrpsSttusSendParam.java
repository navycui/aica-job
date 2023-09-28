package aicluster.pms.api.bsns.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class EntrpsSttusSendParam implements Serializable {
	private static final long serialVersionUID = 7043141688476328201L;
	/** 신청ID */
	List<String> applyIdList;
	/** 과제참여기업ID */
	List<String> taskPartcptnEntrprsIdList;
	/** 발송방법 */
	private String sndngMth;
	/** 발송내용 */
	private String sndngCn;
}
