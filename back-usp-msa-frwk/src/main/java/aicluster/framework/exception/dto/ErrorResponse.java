package aicluster.framework.exception.dto;

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
public class ErrorResponse {
	private String error;
	private String message;
	private Integer status;
	private List<ErrorResponseItem> errors;
	
	public List<ErrorResponseItem> getErrors() {
		List<ErrorResponseItem> errors = new ArrayList<>();
		if(this.errors != null) {
			errors.addAll(this.errors);
		}
		return errors;
	}
	
	public void setErrors(List<ErrorResponseItem> errors) {
		this.errors = new ArrayList<>();
		if(errors != null) {
			this.errors.addAll(errors);
		}
	}
}
