package aicluster.common.common.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DayMemberSummary implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -3355949526303487265L;
	private Long yesterday;
	private Long today;
}
