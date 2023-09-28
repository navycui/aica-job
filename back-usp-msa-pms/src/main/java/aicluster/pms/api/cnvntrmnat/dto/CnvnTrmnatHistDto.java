package aicluster.pms.api.cnvntrmnat.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.entity.UsptBsnsCnvnTrmnatHist;
import lombok.Data;

@Data
public class CnvnTrmnatHistDto implements Serializable{
	/**
	 *전자협약 해지 관리_admin_처리이력
	 */
	private static final long serialVersionUID = 5736453466479208370L;
	/**사업협약해지이력*/
	List<UsptBsnsCnvnTrmnatHist> usptBsnsCnvnTrmnatHistList;
}
