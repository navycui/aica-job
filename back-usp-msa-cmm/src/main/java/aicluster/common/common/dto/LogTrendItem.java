package aicluster.common.common.dto;

import bnet.library.util.CoreUtils.string;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogTrendItem {
	private String date;
	private Long cnt;

	public String getYear() {
		if (string.length(date) >= 4) {
			return string.substring(date, 0, 4);
		}
		return "";
	}
	public String getMonth() {
		if (string.length(date) >= 6) {
			return string.substring(date, 4, 6);
		}
		return "";
	}
	public String getDay() {
		if (string.length(date) >= 8) {
			return string.substring(date, 6, 8);
		}
		return "";
	}
	public String getFmtDate() {
		if (string.length(date) == 6) {
			return getYear() + "-" + getMonth();
		}
		else if (string.length(date) == 8) {
			return getMonth() + "-" + getDay();
		}
		else {
			return "";
		}
	}
}
