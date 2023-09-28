package aicluster.common.common.dto;

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
@AllArgsConstructor
@NoArgsConstructor
public class TermsListItem implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -685157269903979017L;
	private String termsType;
	private String termsTypeNm;
	private String beginDay;

	public String getFmtBeginDay() {
		Date dt = string.toDate(beginDay);
		if (dt == null) {
			return null;
		}
		return date.format(dt, "yyyy-MM-dd");
	}
}
