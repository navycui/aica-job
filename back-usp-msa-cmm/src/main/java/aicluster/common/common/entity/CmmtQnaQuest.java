package aicluster.common.common.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import bnet.library.util.CoreUtils.date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmmtQnaQuest implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 3968112303972794825L;
	private String questId;
	private String qnaId;
	private String categoryCd;
	private String categoryNm;
	private String questSt;
	private String questStNm;
	private Date questStDt;
	private String title;
	private String question;
	private String questAttachmentGroupId;
	private String questionerId;
	private Date questCreatedDt;
	@JsonIgnore
	private Date questUpdatedDt;
	private String answer;
	private String answerAttachmentGroupId;
	@JsonIgnore
	private String answerCreatorId;
	@JsonIgnore
	private Date answerCreatedDt;
	private String answerUpdaterId;
	private Date answerUpdatedDt;

	/*
	 * Helper
	 */
	private String questionerNm;
	private String answererNm;

	private List<CmmtAtchmnfl> questAttachmentList;
	private List<CmmtAtchmnfl> answerAttachmentList;

	private CmmtMberInfo questioner;
	private CmmtEmpInfo answerer;

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
	
	public Date getQuestStDt() {
		if (this.questStDt != null) {
			return new Date(this.questStDt.getTime());
		}
		return null;
	}
	
	public void setQuestStDt(Date questStDt) {
		this.questStDt = null;
		if (questStDt != null) {
			this.questStDt = new Date(questStDt.getTime());
		}
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
	
	public Date getQuestUpdatedDt() {
		if (this.questUpdatedDt != null) {
			return new Date(this.questUpdatedDt.getTime());
		}
		return null;
	}
	
	public void setQuestUpdatedDt(Date questUpdatedDt) {
		this.questUpdatedDt = null;
		if (questUpdatedDt != null) {
			this.questUpdatedDt = new Date(questUpdatedDt.getTime());
		}
	}
	
	public Date getAnswerCreatedDt() {
		if (this.answerCreatedDt != null) {
			return new Date(this.answerCreatedDt.getTime());
		}
		return null;
	}
	
	public void setAnswerCreatedDt(Date answerCreatedDt) {
		this.answerCreatedDt = null;
		if (answerCreatedDt != null) {
			this.answerCreatedDt = new Date(answerCreatedDt.getTime());
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
	
	public List<CmmtAtchmnfl> getQuestAttachmentList() {
		List<CmmtAtchmnfl> questAttachmentList = new ArrayList<>();
		if(this.questAttachmentList != null) {
			questAttachmentList.addAll(this.questAttachmentList);
		}
		return questAttachmentList;
	}

	public void setQuestAttachmentList(List<CmmtAtchmnfl> questAttachmentList) {
		this.questAttachmentList = new ArrayList<>();
		if(questAttachmentList != null) {
			this.questAttachmentList.addAll(questAttachmentList);
		}
	}
	
	public List<CmmtAtchmnfl> getAnswerAttachmentList() {
		List<CmmtAtchmnfl> answerAttachmentList = new ArrayList<>();
		if(this.answerAttachmentList != null) {
			answerAttachmentList.addAll(this.answerAttachmentList);
		}
		return answerAttachmentList;
	}

	public void setAnswerAttachmentList(List<CmmtAtchmnfl> answerAttachmentList) {
		this.answerAttachmentList = new ArrayList<>();
		if(answerAttachmentList != null) {
			this.answerAttachmentList.addAll(answerAttachmentList);
		}
	}
}
