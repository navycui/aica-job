package aicluster.mvn.api.resource.dto;

import java.io.Serializable;

import org.apache.commons.lang3.BooleanUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlrsrcDstbInsListItem implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1426273624657557775L;

	private String rsrcId;			/** 자원ID */
	private Boolean rsrcUseYn;		/** 사용여부 */
	private Long rsrcDstbQy;		/** 자원배분량 */
	private String rsrcDstbCn;		/** 자원배분내용 */

	public long getRsrcDstbQy() {
		if (this.rsrcDstbQy == null) {
			if ( BooleanUtils.isNotTrue(this.rsrcUseYn)) {
				return 0L;
			}
			else {
				return 1L;
			}
		}
		return this.rsrcDstbQy;
	}

	public boolean getRsrcUseYn() {
		if (this.rsrcUseYn == null) {
			if ( this.rsrcDstbQy != null && this.rsrcDstbQy > 0L) {
				return true;
			}
			else {
				return false;
			}
		}
		return this.rsrcUseYn;
	}
}
