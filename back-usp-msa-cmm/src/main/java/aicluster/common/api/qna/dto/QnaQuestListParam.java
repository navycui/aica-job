package aicluster.common.api.qna.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import bnet.library.util.CoreUtils.date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QnaQuestListParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -2736576607451244253L;

	private String questStatus;
	private String categoryCd;
	private String title;
	private String memberNm;
	private Date questBeginDay;
	private Date questEndDay;
	private List<String> questStatusList;

	public String getQuestBeginDay() {
		if (this.questBeginDay == null) {
			return null;
		}
		return date.format(this.questBeginDay, "yyyyMMdd");
	}
	
	public void setQuestBeginDay(Date questBeginDay) {
		this.questBeginDay = null;
		if (questBeginDay != null) {
			this.questBeginDay = new Date(questBeginDay.getTime());
		}
	}

	public String getQuestEndDay() {
		if (this.questEndDay == null) {
			return null;
		}
		return date.format(questEndDay, "yyyyMMdd");
	}
	
	public void setQuestEndDay(Date questEndDay) {
		this.questEndDay = null;
		if (questEndDay != null) {
			this.questEndDay = new Date(questEndDay.getTime());
		}
	}
}
