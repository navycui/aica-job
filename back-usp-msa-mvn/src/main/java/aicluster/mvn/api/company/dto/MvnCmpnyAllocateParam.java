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
public class MvnCmpnyAllocateParam implements Serializable {
    /**
	 *
	 */
	private static final long serialVersionUID = 1576875330195699326L;
	private String mvnId;
    private String mvnFcId;
    private Date mvnBeginDay;
    private Date mvnEndDay;
    private String equipPvdtl;

    public String getFmtMvnBeginDay() {
        if (this.mvnBeginDay == null) {
            return null;
        }
        return date.format(this.mvnBeginDay, "yyyyMMdd");
    }

    public String getFmtMnvEndDay() {
        if (this.mvnEndDay == null) {
            return null;
        }
        return date.format(this.mvnEndDay, "yyyyMMdd");
    }
}
