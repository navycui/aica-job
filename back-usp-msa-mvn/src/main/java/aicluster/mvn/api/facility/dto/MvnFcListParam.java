package aicluster.mvn.api.facility.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MvnFcListParam implements Serializable {
    /**
	 *
	 */
	private static final long serialVersionUID = 8942216525295904303L;
	private String mvnFcNm;
    private String mvnFcType;
    private String mvnFcDtype;
    private Boolean enabled;
}
