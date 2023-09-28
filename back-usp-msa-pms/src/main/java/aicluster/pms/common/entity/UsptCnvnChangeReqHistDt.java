package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import bnet.library.util.CoreUtils.aes256;
import bnet.library.util.CoreUtils.string;
import lombok.Data;

@Data
public class UsptCnvnChangeReqHistDt implements Serializable {
	/**
	 * 협약변경요청_이력
	 */
	private static final long serialVersionUID = -6357181504554185011L;

	/*협약변경요청ID*/
	String cnvnChangeReqId;
	/** 생성일자 */
	String changeDe;
	/** 생성일시 */
	String changeDt;
}
