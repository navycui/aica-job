package aicluster.common.api.board.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardArticleUrlParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 3465976726427522572L;
	private String urlNm;
	private String url;
}
