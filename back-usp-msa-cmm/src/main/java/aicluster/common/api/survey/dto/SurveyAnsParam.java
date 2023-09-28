package aicluster.common.api.survey.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurveyAnsParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 6605606458542593699L;
	private String surveyId;
	private List<SurveyQuestionAns> questions;
	
	public List<SurveyQuestionAns> getQuestions() {
		List<SurveyQuestionAns> questions = new ArrayList<>();
		if(this.questions != null) {
			questions.addAll(this.questions);
		}
		return questions;
	}

	public void setQuestions(List<SurveyQuestionAns> questions) {
		this.questions = new ArrayList<>();
		if(questions != null) {
			this.questions.addAll(questions);
		}
	}
}
