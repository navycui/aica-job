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
public class SurveyQuestionAns implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -3127165855845468789L;
	private String questionId;
	private List<SurveyAnswer> answers;
	
	public List<SurveyAnswer> getAnswers() {
		List<SurveyAnswer> answers = new ArrayList<>();
		if(this.answers != null) {
			answers.addAll(this.answers);
		}
		return answers;
	}

	public void setAnswers(List<SurveyAnswer> answers) {
		this.answers = new ArrayList<>();
		if(answers != null) {
			this.answers.addAll(answers);
		}
	}
}
