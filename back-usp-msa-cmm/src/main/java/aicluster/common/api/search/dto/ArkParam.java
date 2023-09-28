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
public class ArkParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5739928566931893325L;
	
	private String target;
	private String query;
	private String convert;
	private String charset;
	private String datatype;
}
