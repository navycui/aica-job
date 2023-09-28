package aicluster.mvn.api.company.dto;

import java.io.Serializable;
import java.util.Date;

import bnet.library.util.CoreUtils.date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MvnCmpnyCheckoutParam implements Serializable {
    /**
	 *
	 */
	private static final long serialVersionUID = -1092820544670983892L;
	private String checkoutReqId;
	private String mvnId;
    private Date checkoutPlanDay;
    private String checkoutReason;
    private String equipRtdtl;

    public String getFmtCheckoutPlanDay() {
        if (this.checkoutPlanDay == null) {
            return null;
        }
        return date.format(this.checkoutPlanDay, "yyyyMMdd");
    }
}
