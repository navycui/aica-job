package aicluster.pms.common.entity;

import java.io.Serializable;

import aicluster.pms.api.bsns.dto.RsltIdxStdIem;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptStdrRsltIdxStdIem extends RsltIdxStdIem implements Serializable {
	private static final long serialVersionUID = -3965453968959396857L;
}
