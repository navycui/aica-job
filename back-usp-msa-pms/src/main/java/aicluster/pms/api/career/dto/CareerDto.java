package aicluster.pms.api.career.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.entity.UsptAcdmcr;
import aicluster.pms.common.entity.UsptCrqfc;
import aicluster.pms.common.entity.UsptEtcCareer;
import aicluster.pms.common.entity.UsptJobCareer;
import aicluster.pms.common.entity.UsptMsvc;
import aicluster.pms.common.entity.UsptProgrm;
import aicluster.pms.common.entity.UsptWnpz;
import lombok.Data;

@Data
public class CareerDto implements Serializable {
	private static final long serialVersionUID = 1561197520433024304L;
	private UsptMsvc msvcInfo;
	List<UsptAcdmcr> acdmcrList;
	List<UsptCrqfc> crqfcList;
	List<UsptJobCareer> jobCareerList;
	List<UsptEtcCareer> etcCareerList;
	List<UsptWnpz> wnpzList;
	List<UsptProgrm> progrmList;
}
