package aicluster.pms.common.entity;

import java.io.Serializable;

import aicluster.pms.api.bsns.dto.BhExmntIem;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UsptStdrBhExmntIem extends BhExmntIem implements Serializable {
	private static final long serialVersionUID = 8323059119090262052L;
}
