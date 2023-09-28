package aicluster.mvn.api.status.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MvnStatusListParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 8934840304628224451L;
	private String mvnSt;
	private String bnoCd;
	private String mvnFcCapPd;
	private String mvnFcarPd;
	private Long mvnFcCapacity;
	private String mvnFcar;
}
