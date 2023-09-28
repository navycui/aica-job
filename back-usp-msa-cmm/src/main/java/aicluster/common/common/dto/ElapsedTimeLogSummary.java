package aicluster.common.common.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElapsedTimeLogSummary implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 5676497689592289608L;
	private Long max;
	private Long min;
	private Long avg;
}
