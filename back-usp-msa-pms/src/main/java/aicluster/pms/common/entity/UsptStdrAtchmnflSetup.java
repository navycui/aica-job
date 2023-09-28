package aicluster.pms.common.entity;

import java.io.Serializable;

import aicluster.pms.api.bsns.dto.AtchmnflSetup;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptStdrAtchmnflSetup extends AtchmnflSetup implements Serializable {
	private static final long serialVersionUID = -581868146489201996L;
	private String stdrBsnsCd;			/** 기준사업코드 */
}
