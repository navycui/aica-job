package aicluster.pms.common.entity;

import java.io.Serializable;

import aicluster.pms.api.bsns.dto.RsltIdxDetailIem;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptStdrRsltIdxDetailIem extends RsltIdxDetailIem implements Serializable {
	private static final long serialVersionUID = 1698104967943673261L;
}
