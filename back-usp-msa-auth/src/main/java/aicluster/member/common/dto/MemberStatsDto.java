package aicluster.member.common.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bnet.library.util.CoreUtils.date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberStatsDto implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 6168972701579412985L;

	@JsonIgnore
	private Date crtrDt;
	private Integer crtrTotalMbrCnt;
	private Integer crtrSecessionMbrCnt;
	private Integer crtrDormantMbrCnt;
	private Integer crtrBlackMbrCnt;
	private Integer crtrNormalMbrCnt;

	List<MemberStatsListItemDto> statsList;

	public String getFmtCrtrDt() {
		return date.format(crtrDt, "yyyy-MM-dd HH:mm");
	}

	public Date getCrtrDt() {
		if (this.crtrDt != null) {
            return new Date(this.crtrDt.getTime());
        }
        return null;
	}

	public void setCrtrDt(Date crtrDt) {
		this.crtrDt = null;
        if (crtrDt != null) {
            this.crtrDt = new Date(crtrDt.getTime());
        }
	}

}
