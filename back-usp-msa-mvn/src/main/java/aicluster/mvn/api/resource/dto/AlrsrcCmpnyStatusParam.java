package aicluster.mvn.api.resource.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlrsrcCmpnyStatusParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 4437924951368660310L;

	private String alrsrcId;
	private String alrsrcSt;
	private String reasonCn;
}
