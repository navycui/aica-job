package aicluster.member.common.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmmtSysCharger implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 3229937065366156631L;

	private String apiSysId;
	private String chargerEmail;
	private String chargerNm;
}
