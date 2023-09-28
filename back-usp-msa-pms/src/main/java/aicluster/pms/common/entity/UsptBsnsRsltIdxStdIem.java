package aicluster.pms.common.entity;

import java.io.Serializable;

import aicluster.pms.api.bsns.dto.RsltIdxStdIem;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptBsnsRsltIdxStdIem extends RsltIdxStdIem implements Serializable {
	private static final long serialVersionUID = -2485776195834760390L;
}
