package aicluster.pms.api.bsnsapply.dto;

import java.io.Serializable;

import aicluster.pms.common.entity.CmmtEnt;
import aicluster.pms.common.entity.CmmtMember;
import aicluster.pms.common.entity.UsptBsnsPblancApplcntEnt;
import lombok.Data;

@Data
public class FrontBsnsApplicantDto implements Serializable {
	private static final long serialVersionUID = 7456898335707662867L;
	private CmmtMember cmmtMember;
	private CmmtEnt cmmtEnt;
	private UsptBsnsPblancApplcntEnt applcntEnt;
}
