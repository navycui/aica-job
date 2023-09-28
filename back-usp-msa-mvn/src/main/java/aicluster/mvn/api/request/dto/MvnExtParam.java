package aicluster.mvn.api.request.dto;

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
public class MvnExtParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -3079332277469874897L;
	private String mvnId;
	private String mvnEtReqId;
	private Date mvnEtEndDay;
	private String mvnEtReqCn;

	public String getMvnEtEndDay() {
		if (this.mvnEtEndDay == null) {
			return null;
		}
		return date.format(this.mvnEtEndDay, "yyyyMMdd");
	}
}
