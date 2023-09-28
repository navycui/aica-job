package aicluster.mvn.common.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckoutReqStCountDto implements Serializable {

	private static final long serialVersionUID = 626287840798254230L;

	private Long aplyCount;
	private Long rtrcnCount;
	private Long splmntCount;
	private Long aprvCount;
}
