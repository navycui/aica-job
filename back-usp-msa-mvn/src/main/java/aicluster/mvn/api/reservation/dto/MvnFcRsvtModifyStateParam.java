package aicluster.mvn.api.reservation.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MvnFcRsvtModifyStateParam implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 3256759319108377025L;
	private String reserveSt;
	private String rejectReasonCn;
}
