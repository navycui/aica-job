package aicluster.common.api.search.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchPopwordDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5769962665044432438L;
	
	private List<SearchPopwordResultDto> result;
	private long totalCnt;
	private long errorCode;
	private String status;
	private String errorMsg;
	
	public List<SearchPopwordResultDto> getResult() {
		List<SearchPopwordResultDto> result = new ArrayList<>();
		if(this.result != null) {
			result.addAll(this.result);
		}
		return result;
	}

	public void setResult(List<SearchPopwordResultDto> result) {
		this.result = new ArrayList<>();
		if(result != null) {
			this.result.addAll(result);
		}
	}
}
