package aicluster.member.api.insider.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsiderListParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 3409289230508318341L;

	private String loginId;
	private String memberNm;
	private String memberSt;
	private String authorityId;
	private String deptNm;
}
