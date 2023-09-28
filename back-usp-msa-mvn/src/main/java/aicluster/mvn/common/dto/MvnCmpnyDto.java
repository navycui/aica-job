package aicluster.mvn.common.dto;

import aicluster.mvn.common.entity.UsptMvnEntrpsInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class MvnCmpnyDto extends UsptMvnEntrpsInfo {
	/**
	 *
	 */
	private static final long serialVersionUID = -2992681919265374168L;

	private String recentPresentnYm;	/** 입주업체성과 최근제출년월 */
	private MvnUserDto mvnUser;		/** 입주자 정보 */
	private MvnCmpnyChcktDto mvnCheckout;	/** 입주업체 퇴실정보 */
}
