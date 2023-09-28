package aicluster.mvn.api.resource.dto;

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
public class AlrsrcStatusParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 936317417171167347L;

	private Date alrsrcBgngDay  ;      /* 자원할당시작일                              */
	private Date alrsrcEndDay   ;      /* 자원할당종료일                              */
	private Boolean ordtmRcrit  ;      /* 상시모집여부(null:전체, true:상시모집, false:정시모집) */
	private String cmpnyNm      ;      /* 업체명                                      */

	public String getAlrsrcBgngDay() {
		if (this.alrsrcBgngDay == null) {
			return null;
		}
		return date.format(this.alrsrcBgngDay, "yyyyMMdd");
	}

	public String getAlrsrcEndDay() {
		if (this.alrsrcEndDay == null) {
			return null;
		}
		return date.format(this.alrsrcEndDay, "yyyyMMdd");
	}
}
