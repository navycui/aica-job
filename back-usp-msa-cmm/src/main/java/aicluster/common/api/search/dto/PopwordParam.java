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
public class PopwordParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6020474684251726524L;
	
	private String target;
	private String collection;
	private String range;
	private String datatype;
}
