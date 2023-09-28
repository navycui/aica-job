package aicluster.framework.common.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionKeyDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 5404304139373965844L;

	private String key;
}
