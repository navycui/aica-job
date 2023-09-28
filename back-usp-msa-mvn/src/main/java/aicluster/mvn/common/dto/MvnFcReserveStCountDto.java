package aicluster.mvn.common.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MvnFcReserveStCountDto implements Serializable {

	private static final long serialVersionUID = -3081808447098713407L;

	private Long aplyCount;
	private Long rjctCount;
	private Long aprvCount;
	private Long rsvtRtrcnCount;
	private Long aprvRtrcnCount;
}
