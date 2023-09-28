package aicluster.common.common.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QnaQuestStatusCountItem implements Serializable {

	private static final long serialVersionUID = 5115998939268247894L;

	private String qnaId;
	private Long requestCount;
	private Long receiptCount;
	private Long answerCount;
	private Long confirmCount;
}
