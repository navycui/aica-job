package aicluster.mvn.api.request.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MvnExtListParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -5007235310134328642L;
	private String mvnEtReqSt;
	private String bnoRoomNo;
	private String memberNm;
}
