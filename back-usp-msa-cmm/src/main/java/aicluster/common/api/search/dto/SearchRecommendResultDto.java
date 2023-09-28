package aicluster.common.api.search.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchRecommendResultDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4267924381493409638L;
	
	private List<String> result;
	private long totalCnt;
	private long errorCode;
	private String status;
	private String errorMsg;
}
