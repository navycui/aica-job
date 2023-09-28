package aicluster.common.common.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardArticlePrevNextItem implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -2870507683088610834L;

	private String boardId;
	private String articleId;
	private String title;
	private String prevArticleId;
	private String prevTitle;
	private String nextArticleId;
	private String nextTitle;
}
