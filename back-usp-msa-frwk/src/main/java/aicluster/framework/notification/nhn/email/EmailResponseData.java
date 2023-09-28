package aicluster.framework.notification.nhn.email;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailResponseData {
	private String requestId;
	private List<EmailResponseResult> results;
	
	public List<EmailResponseResult> getResults() {
		List<EmailResponseResult> results = new ArrayList<>();
		if(this.results != null) {
			results.addAll(this.results);
		}
		return results;
	}
	
	public void setResults(List<EmailResponseResult> results) {
		this.results = new ArrayList<>();
		if(results != null) {
			this.results.addAll(results);
		}
	}
}
