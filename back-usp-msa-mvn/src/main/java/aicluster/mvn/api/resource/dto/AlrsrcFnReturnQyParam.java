package aicluster.mvn.api.resource.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlrsrcFnReturnQyParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 6973716771859015644L;

	private String rsrcId;
	private int returnQy;
}
