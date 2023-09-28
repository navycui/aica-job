package aicluster.pms.common.entity;

import java.io.Serializable;

import aicluster.pms.api.bsns.dto.Charger;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptBsnsCharger extends Charger implements Serializable {
	private static final long serialVersionUID = -8625592771941765777L;
	private String bsnsCd;				/** 사업코드 */
}
