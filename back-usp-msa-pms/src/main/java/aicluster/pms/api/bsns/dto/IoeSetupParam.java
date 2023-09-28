package aicluster.pms.api.bsns.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.entity.UsptWctTaxitm;
import lombok.Data;

@Data
public class IoeSetupParam implements Serializable {

	private static final long serialVersionUID = -3105715386422628255L;

	private String wctIoeId;		/** 사업비비목ID */
	List<UsptWctTaxitm> taxitmList;
}
