package aicluster.pms.api.bsns.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.entity.UsptStdrBsns;
import aicluster.pms.common.entity.UsptStdrCharger;
import aicluster.pms.common.entity.UsptStdrRecomendCl;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StdrBsnsBasicParam extends UsptStdrBsns implements Serializable {
	private static final long serialVersionUID = 3177657149373470121L;
	/** 추천분류 */
	List<UsptStdrRecomendCl> stdrRecomendClList;
	/** 담당자 */
	List<UsptStdrCharger> stdrChargerList;
}