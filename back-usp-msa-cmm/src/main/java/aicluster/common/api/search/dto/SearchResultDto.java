package aicluster.common.api.search.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 9192064494403337317L;

	private Map<String, Object> result;
	private long totalCnt;
	private long errorCode;
	private String status;
	private String errorMsg;
	
	public void setResult(Map<String, Object> result) {
		this.result = new HashMap<>();
		if(result != null) {
			this.result = new HashMap<>(result.size());
			for(String key:result.keySet()){
			  this.result.put(key, result.get(key));
			}
		}
	}

	public Map<String, Object> getResult() {
		Map<String, Object> result = new HashMap<>();
		for(String key:this.result.keySet()){
			result.put(key, this.result.get(key));
		}
		return result;
	}
}
