package aicluster.pms.common.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class UsptBsnsPblancApplyAtchmnfl implements Serializable {
	private static final long serialVersionUID = -5669810591449060590L;
	/** 신청ID */
	private String applyId;
	/** 첨부파일설정ID */
	private String atchmnflSetupId;
	/** 첨부파일ID */
	private String atchmnflId;
	/** 동일비율 */
	private Float samenssRate;
	/** 생성자ID */
	private String creatrId;
	/** 생성일시 */
	private Date creatDt;
	/** 수정자ID */
	private String updusrId;
	/** 수정일시 */
	private Date updtDt;
}
