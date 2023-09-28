package aicluster.member.api.module.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PkiResultDto implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -6753798912841117584L;

	private String signedResult;
}
