package aicluster.pms.common.entity;

import java.io.Serializable;

import aicluster.pms.api.bsns.dto.RsltIdx;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptStdrRsltIdx extends RsltIdx implements Serializable {
	private static final long serialVersionUID = 4775223514056064791L;
	private String stdrBsnsCd;			/** 기준사업코드 */
}
