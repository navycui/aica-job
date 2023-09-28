package aicluster.common.common.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import aicluster.common.common.dto.ResultAnswerDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmmtQustnrQesitm implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -4078308762411950630L;
	private String surveyId;
	private String questionId;
	private Integer questionNo;
	private String questionType;
	private Boolean required;
	private String questionCn;

	private Integer answererCnt;
	private List<ResultAnswerDto> resultAnswerList;
	private List<CmmtQustnrRspns> answerList;
	
	public List<ResultAnswerDto> getResultAnswerList() {
		List<ResultAnswerDto> resultAnswerList = new ArrayList<>();
		if(this.resultAnswerList != null) {
			resultAnswerList.addAll(this.resultAnswerList);
		}
		return resultAnswerList;
	}

	public void setResultAnswerList(List<ResultAnswerDto> resultAnswerList) {
		this.resultAnswerList = new ArrayList<>();
		if(resultAnswerList != null) {
			this.resultAnswerList.addAll(resultAnswerList);
		}
	}
	
	public List<CmmtQustnrRspns> getAnswerList() {
		List<CmmtQustnrRspns> answerList = new ArrayList<>();
		if(this.answerList != null) {
			answerList.addAll(this.answerList);
		}
		return answerList;
	}

	public void setAnswerList(List<CmmtQustnrRspns> answerList) {
		this.answerList = new ArrayList<>();
		if(answerList != null) {
			this.answerList.addAll(answerList);
		}
	}
}
