package aicluster.pms.api.bsnsplan.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class BsnsPlanDocIdDto implements Serializable{

	/**
	 *사업계획서 접수관리
	 */
	private static final long serialVersionUID = 9096324573815083219L;
	 /** 사업계획서ID */
	String  bsnsPlanDocId;
}
