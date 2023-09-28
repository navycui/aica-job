package aicluster.common.api.survey.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 4096841369613989575L;
	private String questionType;
	private Boolean required;
	private String questionCn;
	private List<String> answerCnList;
	
	public List<String> getAnswerCnList() {
		List<String> answerCnList = new ArrayList<>();
		if(this.answerCnList != null) {
			answerCnList.addAll(this.answerCnList);
		}
		return answerCnList;
	}

	public void setAnswerCnList(List<String> answerCnList) {
		this.answerCnList = new ArrayList<>();
		if(answerCnList != null) {
			this.answerCnList.addAll(answerCnList);
		}
	}
}
