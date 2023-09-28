package aicluster.common.common.dto;

import java.io.Serializable;
import java.util.Date;

import bnet.library.util.CoreUtils.date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QnaQuestListItem implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -2820109788620702694L;

	private String questId;
	private String qnaId;
	private String categoryCd;
	private String categoryNm;
	private String title;
	private String questSt;
	private String questStNm;
	private String questionerId;
	private String questionerNm;
	private Date questCreatedDt;
	private String answererId;
	private String answererNm;
	private Date answerUpdatedDt;
	private Long rn;

	public String getFmtQuestCreatedDt() {
		if (this.questCreatedDt == null) {
			return null;
		}
		return date.format(this.questCreatedDt, "yyyy-MM-dd HH:mm:ss");
	}

	public String getFmtAnswerUpdateDt() {
		if (this.answerUpdatedDt == null) {
			return null;
		}
		return date.format(this.answerUpdatedDt, "yyyy-MM-dd HH:mm:ss");
	}
	
	public Date getQuestCreatedDt() {
		if (this.questCreatedDt != null) {
			return new Date(this.questCreatedDt.getTime());
		}
		return null;
	}
	
	public void setQuestCreatedDt(Date questCreatedDt) {
		this.questCreatedDt = null;
		if (questCreatedDt != null) {
			this.questCreatedDt = new Date(questCreatedDt.getTime());
		}
	}
	
	public Date getAnswerUpdatedDt() {
		if (this.answerUpdatedDt != null) {
			return new Date(this.answerUpdatedDt.getTime());
		}
		return null;
	}
	
	public void setAnswerUpdatedDt(Date answerUpdatedDt) {
		this.answerUpdatedDt = null;
		if (answerUpdatedDt != null) {
			this.answerUpdatedDt = new Date(answerUpdatedDt.getTime());
		}
	}
}
