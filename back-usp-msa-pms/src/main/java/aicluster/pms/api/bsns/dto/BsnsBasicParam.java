package aicluster.pms.api.bsns.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.entity.UsptBsns;
import aicluster.pms.common.entity.UsptBsnsCharger;
import aicluster.pms.common.entity.UsptBsnsRecomendCl;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class BsnsBasicParam extends UsptBsns implements Serializable {
	private static final long serialVersionUID = 5770618323456939881L;
	/** 추천분류 */
	List<UsptBsnsRecomendCl> bsnsRecomendClList;
	/** 담당자 */
	List<UsptBsnsCharger> bsnsChargerList;
}
