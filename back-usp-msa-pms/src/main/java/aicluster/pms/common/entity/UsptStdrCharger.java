package aicluster.pms.common.entity;

import java.io.Serializable;

import aicluster.pms.api.bsns.dto.Charger;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptStdrCharger extends Charger implements Serializable {
	private static final long serialVersionUID = -2641847423262039734L;
	private String stdrBsnsCd;			/** 기준사업코드 */
}
