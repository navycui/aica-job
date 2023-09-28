package aicluster.mvn.common.dto;

import java.io.Serializable;
import java.util.Date;

import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MvnCmpnyChcktDto implements Serializable {

	private static final long serialVersionUID = -7790368365900695153L;

    private String  checkoutPlanDay;	/** 퇴실예정일 */
    private String  checkoutReason ;	/** 퇴실사유 */
    private Date    checkoutReqStDt;	/** 퇴실신청상태변경일시 */
    private String  equipRtdtl     ;	/** 장비반납내역 */
    private String  workerId       ;	/** 처리자ID */
    private String  workerNm       ;	/** 처리자명 */

    public String getFmtCheckoutPlanDay() {
        if (this.checkoutPlanDay == null || date.isValidDate(this.checkoutPlanDay, "yyyyMMdd")) {
            return null;
        }
        return date.format(string.toDate(this.checkoutPlanDay), "yyyy-MM-dd");
    }

    public String getFmtCheckoutReqStDt() {
        if (this.checkoutReqStDt == null) {
            return null;
        }
        return date.format(this.checkoutReqStDt, "yyyy-MM-dd HH:mm:ss");
    }
}
