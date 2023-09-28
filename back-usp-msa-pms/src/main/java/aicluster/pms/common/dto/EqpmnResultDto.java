package aicluster.pms.common.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import aicluster.pms.common.util.Code;
import bnet.library.util.CoreUtils;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;
import lombok.Data;

@Data
public class EqpmnResultDto implements Serializable {
	private static final long serialVersionUID = -3526899709123849691L;
	/** 통계일 */
	private String statsDe;
	/** 견적신청 */
	private Integer estmtReqst;
	/** 사용신청 */
	private Integer useReqst;
	/** 승인 */
	private Integer confm;
	/** 사용 */
	private Integer use;
	/** 종료 */
	private Integer useEnd;
	/** 사용장비수 */
	private Integer useEqpmnCo;
	/** 반출장비수 */
	private Integer tkoutEqpmnCo;
	/** 반입장비수 */
	private Integer tkinEqpmnCo;
	/** 사용총액 */
	private Long useTotamt;
	/** 미납 */
	private Long npy;
	/** 생성일시 */
	@JsonIgnore
	private Integer creatDt;

	public String getFmtStatsDay() {
		if (string.isBlank(statsDe)) {
			return "";
		}
		else if (string.length(statsDe) == 6) {
			return string.substring(statsDe, 0, 4) + "-" + string.substring(statsDe, 4, 6);
		}
		else if (string.length(statsDe) == 8) {
			String str = Code.week[date.getDayOfWeek(string.toDate(statsDe))];
			if(CoreUtils.string.isNotBlank(str)) {
				str = "(" + str + ")";
			}
			return string.substring(statsDe, 0, 4) + "-" + string.substring(statsDe, 4, 6) + "-" + string.substring(statsDe, 6, 8) + str;
		}
		return "";
	}
}