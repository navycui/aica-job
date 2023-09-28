package aicluster.mvn.api.reservation.dto;

import java.io.Serializable;

import aicluster.mvn.common.entity.UsptMvnFcltyInfo;
import aicluster.mvn.common.entity.UsptMvnFcltyResve;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MvnFcRsvtDto implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -6439498776612515488L;
	private UsptMvnFcltyResve mvnFcRsvt;
	private UsptMvnFcltyInfo mvnFc;
	private MvnFcRsvctmDto rsvctm;
}
