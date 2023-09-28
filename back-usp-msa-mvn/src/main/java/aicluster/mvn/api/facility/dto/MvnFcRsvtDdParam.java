package aicluster.mvn.api.facility.dto;

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
public class MvnFcRsvtDdParam implements Serializable {
    /**
	 *
	 */
	private static final long serialVersionUID = -5831029830086012514L;
	private Date beginDay;
    private Date endDay;
    private String reason;

    public String getBeginDay() {
        if (this.beginDay == null) {
            return null;
        }
        return date.format(this.beginDay, "yyyyMMdd");
    }

    public String getEndDay() {
        if (this.endDay == null) {
            return null;
        }
        return date.format(this.endDay, "yyyyMMdd");
    }

    public String getFmtBeginDay() {
        if (this.beginDay == null) {
            return null;
        }
        return date.format(this.beginDay, "yyyy-MM-dd");
    }

    public String getFmtEndDay() {
        if (this.endDay == null) {
            return null;
        }
        return date.format(this.endDay, "yyyy-MM-dd");
    }
}
