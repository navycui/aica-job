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
public class CmmtBbscttCn implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 167420673842846491L;
	private String articleCnId;
	private String articleId;
	private Long sortOrder;
	private String header;
	private String articleCn;
}
