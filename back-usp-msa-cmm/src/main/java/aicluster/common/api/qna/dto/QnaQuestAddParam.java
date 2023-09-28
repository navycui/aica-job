package aicluster.common.api.qna.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QnaQuestAddParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -1256324135559882246L;
	private String qnaId;
	private String categoryCd;
	private String title;
	private String question;
}
