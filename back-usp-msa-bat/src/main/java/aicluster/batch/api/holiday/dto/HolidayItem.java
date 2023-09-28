package aicluster.batch.api.holiday.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HolidayItem {
	private String dateKind;
	private String dateName;
	private String isHoliday;
	private String locdate;
	private Integer seq;
}
