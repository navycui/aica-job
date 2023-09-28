package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class MyBsnsNtcnListDto implements Serializable {
	private static final long serialVersionUID = -5736096499615042476L;
	/** 작업명 */
	private String jobNm;
	/** 공고명 */
	private String pblancNm;
	/** 상태명 */
	private String sttusNm;
}
