package aicluster.pms.api.bsnsapply.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class SamenssRateParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -944611767649764460L;
	/** 신청ID */
	private String applyId;
	/** 첨부파일ID */
	private String atchmnflId;

	private Long beginRowNum;
	private Long itemsPerPage;
}
