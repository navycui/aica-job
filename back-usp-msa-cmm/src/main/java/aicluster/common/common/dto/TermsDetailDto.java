package aicluster.common.common.dto;

import java.io.Serializable;

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
public class TermsDetailDto implements Serializable {

    private static final long serialVersionUID = -4111383759048201062L;

    private String termsType;
    private String termsTypeNm;
    private String beginDay;
    private String requiredTermsCn;
    private String optionedTermsCn;
    private String possessTermCd;
    private String possessTermNm;

	public String getFmtBeginDay() {
		if (string.isBlank(this.beginDay) || !date.isValidDate(this.beginDay, "yyyyMMdd")) {
			return null;
		}
		return date.format(string.toDate(this.beginDay), "yyyy-MM-dd");
	}
}
