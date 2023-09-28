package aicluster.mvn.api.company.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MvnCmpnyListParam implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = -397206744375979628L;
	private String mvnCmpnySt;
    private String mvnAllocateSt;
    private String bnoRoomNo;
    private Boolean enabled;
    private String cmpnyNm;
}
