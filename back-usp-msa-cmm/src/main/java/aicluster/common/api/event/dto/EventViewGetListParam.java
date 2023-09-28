package aicluster.common.api.event.dto;

import java.io.Serializable;
import java.util.Date;

import aicluster.common.common.util.CodeExt;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventViewGetListParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 8001474880138218368L;

	private Date beginDay;
	private Date endDay;
	private String searchType;
	private String searchCn;
	private String sortType;

	public String getBeginDay() {
		if (this.beginDay == null) {
			return null;
		}
		return date.format(this.beginDay, "yyyyMMdd");
	}
	
	public void setBeginDay(Date beginDay) {
		this.beginDay = null;
		if (beginDay != null) {
			this.beginDay = new Date(beginDay.getTime());
		}
	}

	public String getEndDay() {
		if (this.endDay == null) {
			return null;
		}
		return date.format(this.endDay, "yyyyMMdd");
	}
	
	public void setEndDay(Date endDay) {
		this.endDay = null;
		if (endDay != null) {
			this.endDay = new Date(endDay.getTime());
		}
	}

	public String getSearchType() {
		if (string.isBlank(this.searchType)) {
			return CodeExt.searchType.전체;
		}
		return this.searchType;
	}

	public String getSortType() {
		if (string.isBlank(this.sortType)) {
			return CodeExt.eventSortType.생성일시;
		}
		return string.upperCase(this.sortType);
	}
}
