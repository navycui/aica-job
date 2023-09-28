package aicluster.common.common.dto;

import java.io.Serializable;

public class ResultAnswerDto implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -3089929013880282441L;
	private String surveyId;
	private String questionId;
	private Integer questionNo;
	private String answerId;
	private Integer answerNo;
	private String shortAnswer;
	private Integer cnt;

	public String getSurveyId() {
		return surveyId;
	}
	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public Integer getQuestionNo() {
		return questionNo;
	}
	public void setQuestionNo(Integer questionNo) {
		this.questionNo = questionNo;
	}
	public String getAnswerId() {
		return answerId;
	}
	public void setAnswerId(String answerId) {
		this.answerId = answerId;
	}
	public Integer getAnswerNo() {
		return answerNo;
	}
	public void setAnswerNo(Integer answerNo) {
		this.answerNo = answerNo;
	}
	public String getShortAnswer() {
		return shortAnswer;
	}
	public void setShortAnswer(String shortAnswer) {
		this.shortAnswer = shortAnswer;
	}
	public Integer getCnt() {
		return cnt;
	}
	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}
}
