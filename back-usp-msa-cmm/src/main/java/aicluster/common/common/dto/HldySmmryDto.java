package aicluster.common.common.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HldySmmryDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 4610248093152253168L;
	private String ym;
	private int totalCnt;
	private int saturdayCnt;
	private int sundayCnt;
	private int holidayCnt;
	private int dsgnHolidayCnt;
	private int exclHolidayCnt;
	private int workingDayCnt;

}
