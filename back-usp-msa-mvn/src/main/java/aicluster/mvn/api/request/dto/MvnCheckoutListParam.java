package aicluster.mvn.api.request.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MvnCheckoutListParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3907242939306571426L;
	private String checkoutReqSt;
	private String bnoRoomNo;
	private String memberNm;
}
