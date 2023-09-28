package aicluster.batch.api.holiday.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidaySaveParam {
	private Integer year;
	private Integer month;
	private Integer cnt;
}
