package aicluster.batch.common.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberStatsItem implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -2590014432847696129L;
	private String memberType;
	private Long cnt;
}
