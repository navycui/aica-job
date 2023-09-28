package aicluster.common.api.search.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchPopwordResultDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1514119808980806941L;

	private long count;
	private long id;
	private String updown;
	private String content;
}
