package aicluster.member.common.dto;

import java.io.Serializable;

import bnet.library.util.CoreUtils.string;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberStatsListItemDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3456176064355963909L;

	private String statsDay;
	private Integer totalMbrCnt;
	private Integer joinMbrCnt;
	private Integer secessionMbrCnt;
	private Integer dormantMbrCnt;
	private Integer blackMbrCnt;

	public String getFmtStatsDay() {
		if (string.isBlank(statsDay)) {
			return "";
		}
		else if (string.length(statsDay) == 6) {
			return string.substring(statsDay, 0, 4) + "-" + string.substring(statsDay, 4, 6);
		}
		else if (string.length(statsDay) == 8) {
			return string.substring(statsDay, 0, 4) + "-" + string.substring(statsDay, 4, 6) + "-" + string.substring(statsDay, 6, 8);
		}
		return "";
	}

}
