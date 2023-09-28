package aicluster.pms.common.entity;

import java.io.Serializable;

import aicluster.pms.api.bsns.dto.RecomendCl;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptStdrRecomendCl extends RecomendCl implements Serializable {
	private static final long serialVersionUID = -3319422358727078077L;
	private String stdrBsnsCd;			/** 기준사업코드 */
}
