package aicluster.common.common.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmmtBbscttLink implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 7395160224073770523L;
	private String urlId;
	private String articleId;
	private Long sortOrder;
	private String urlNm;
	private String url;
}
