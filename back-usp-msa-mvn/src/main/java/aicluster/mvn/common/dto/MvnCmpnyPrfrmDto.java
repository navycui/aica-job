package aicluster.mvn.common.dto;

import aicluster.mvn.common.entity.UsptMvnEntrpsPfmc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class MvnCmpnyPrfrmDto extends UsptMvnEntrpsPfmc {

	/**
	 *
	 */
	private static final long serialVersionUID = -8025113666632131804L;

	private MvnUserDto mvnUser;
}
