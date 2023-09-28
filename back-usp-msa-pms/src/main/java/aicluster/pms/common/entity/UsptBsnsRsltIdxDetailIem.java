package aicluster.pms.common.entity;

import java.io.Serializable;

import aicluster.pms.api.bsns.dto.RsltIdxDetailIem;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptBsnsRsltIdxDetailIem extends RsltIdxDetailIem implements Serializable {
	private static final long serialVersionUID = 5295333939524227244L;
}
