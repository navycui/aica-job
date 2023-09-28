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
public class MvnCheckoutStatusParam implements Serializable {

	private static final long serialVersionUID = -1737651498081974308L;

	private String checkoutReqId;
	private String checkoutReqSt;
	private String equipRtdtl;
	private String makeupReqCn;
}
