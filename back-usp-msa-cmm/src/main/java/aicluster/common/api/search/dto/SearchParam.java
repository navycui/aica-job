package aicluster.common.api.search.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -9014469913413034032L;

	private String searchFlag;
	private String system;
	private String collection;
	private String query;
	private String requery;
	private String exQuery;
	private Integer startCount;
	private Integer listCount;
}
