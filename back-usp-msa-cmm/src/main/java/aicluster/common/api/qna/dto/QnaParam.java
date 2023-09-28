package aicluster.common.api.qna.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import aicluster.common.common.entity.CmmtQna;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QnaParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -8541844816750796558L;
	private CmmtQna qna;
	private List<String> answererList;
	
	public List<String> getAnswererList() {
		List<String> answererList = new ArrayList<>();
		if(this.answererList != null) {
			answererList.addAll(this.answererList);
		}
		return answererList;
	}
	
	public void setAnswererList(List<String> answererList) {
		this.answererList = new ArrayList<>();
		if(answererList != null) {
			this.answererList.addAll(answererList);
		}
	}
}
