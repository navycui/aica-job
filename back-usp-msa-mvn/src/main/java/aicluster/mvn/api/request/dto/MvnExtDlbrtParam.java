package aicluster.mvn.api.request.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MvnExtDlbrtParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3193316512737069369L;
	private String mvnEtReqId;
	private String mvnEtReqSt;
	private Date dlbrtDay;
	private Date dlbrtAprvEndDay;
	private String dlbrtResultDtlCn;
}
