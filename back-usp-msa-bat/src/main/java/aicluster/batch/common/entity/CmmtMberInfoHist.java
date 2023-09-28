package aicluster.batch.common.entity;

import java.io.Serializable;
import java.util.Date;

import bnet.library.util.CoreUtils.date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CmmtMberInfoHist implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 7830009560984838622L;
	private String histId;
	private Date histDt;
	private String	workTypeNm;
	private String memberId;
	private String	workerId;
	private String	workCn;

	public String getFmtHistDt() {
		if (histDt == null) {
			return "";
		}
		return date.format(histDt, "yyyy-MM-dd");
	}
}
