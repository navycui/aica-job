package aicluster.mvn.api.resource.dto;

import java.io.Serializable;
import java.util.Date;

import bnet.library.util.CoreUtils.date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlrsrcCmpnyPeriodParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 3252812874240027197L;

	private String alrsrcId;
	private Date alrsrcEndDay;
	private String reasonCn;

	public String getAlrsrcEndDay() {
		if (this.alrsrcEndDay == null) {
			return null;
		}
		return date.format(this.alrsrcEndDay, "yyyyMMdd");
	}

	public String getFmtAlrsrcEndDay() {
		if (this.alrsrcEndDay == null) {
			return null;
		}
		return date.format(this.alrsrcEndDay, "yyyy-MM-dd");
	}
}
