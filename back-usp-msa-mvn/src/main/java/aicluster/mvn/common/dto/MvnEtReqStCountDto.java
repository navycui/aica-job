package aicluster.mvn.common.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MvnEtReqStCountDto implements Serializable {

	private static final long serialVersionUID = -3430794120083611971L;

	private Long aplyCount;
	private Long splmntCount;
	private Long rcptCount;
	private Long rjctCount;
	private Long aprvCount;
	private Long rtrcnCount;
}
