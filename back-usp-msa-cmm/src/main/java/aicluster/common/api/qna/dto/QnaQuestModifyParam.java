package aicluster.common.api.qna.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QnaQuestModifyParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -1268926033414848147L;
	private String qnaId;
	private String questId;
	private String categoryCd;
	private String title;
	private String question;
}
