package aicluster.pms.api.pblanc.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class SlctnPblancTargetListParam implements Serializable {
	private static final long serialVersionUID = 6796075833401746015L;

	/** 공고명 */
	private String pblancNm;
	/** 로그인 사용자 */
	private String insiderId;

	private Long beginRowNum;
	private Long itemsPerPage;
}
