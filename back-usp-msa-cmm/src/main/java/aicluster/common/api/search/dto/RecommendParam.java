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
public class RecommendParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5888480355146097688L;
	
	private String target;
	private String label;
	private String datatype;
	private String query;
	
}
