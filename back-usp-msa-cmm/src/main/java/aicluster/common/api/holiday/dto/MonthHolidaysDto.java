package aicluster.common.api.holiday.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonthHolidaysDto {

	private String ymd;
	private String ymdNm;
	private boolean holidayYn;

}
