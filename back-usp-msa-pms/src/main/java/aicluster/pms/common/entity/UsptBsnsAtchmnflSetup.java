package aicluster.pms.common.entity;

import java.io.Serializable;

import aicluster.pms.api.bsns.dto.AtchmnflSetup;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptBsnsAtchmnflSetup extends AtchmnflSetup implements Serializable {
	private static final long serialVersionUID = -1128421088899506101L;
	private String bsnsCd;			/** 사업코드 */
}
