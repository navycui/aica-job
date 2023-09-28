package aicluster.mvn.common.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MvnFcCodeDto implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -185849612959076407L;

	private String code;
	private String codeNm;
}
