package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.api.bsns.dto.ApplyMberType;
import aicluster.pms.api.bsns.dto.Chklst;
import lombok.Data;

@Data
public class ApplyLimitDto implements Serializable {
	private static final long serialVersionUID = -4646395214851299944L;
	List<ApplyMberType> applyMberTypeList;
	List<Chklst> chklstList;
}
