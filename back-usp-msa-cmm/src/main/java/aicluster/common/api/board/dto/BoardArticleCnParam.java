package aicluster.common.api.board.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardArticleCnParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 6709254793481971818L;
	private String header;
	private String articleCn;
}
