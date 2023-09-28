package aicluster.pms.common.entity;

import java.io.Serializable;

import aicluster.pms.api.bsns.dto.RecomendCl;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptBsnsRecomendCl extends RecomendCl implements Serializable {
	private static final long serialVersionUID = 2841937273408419486L;
	private String bsnsCd;				/** 사업코드 */
}
