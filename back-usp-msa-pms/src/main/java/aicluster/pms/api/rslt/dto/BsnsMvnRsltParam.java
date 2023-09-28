package aicluster.pms.api.rslt.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class BsnsMvnRsltParam implements Serializable {
	private static final long serialVersionUID = -7273270805254529191L;
	/** 신청ID */
	private String applyId;
	/** 성과년월 */
	private String rsltYm;
}
