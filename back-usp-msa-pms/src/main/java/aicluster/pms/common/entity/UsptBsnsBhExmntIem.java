package aicluster.pms.common.entity;

import java.io.Serializable;

import aicluster.pms.api.bsns.dto.BhExmntIem;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptBsnsBhExmntIem extends BhExmntIem implements Serializable {
	private static final long serialVersionUID = -6595640882181957119L;
}
