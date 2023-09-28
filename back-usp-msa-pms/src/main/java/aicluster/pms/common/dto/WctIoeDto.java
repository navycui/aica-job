package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.entity.UsptWctIoe;
import aicluster.pms.common.entity.UsptWctTaxitm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class WctIoeDto extends UsptWctIoe implements Serializable {
	private static final long serialVersionUID = 289787428732176126L;
	List<UsptWctTaxitm> taxitmList;
}
