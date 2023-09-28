package aicluster.mvn.api.resource.dto;

import java.io.Serializable;

import bnet.library.util.CoreUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlrsrcFoundInfParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -8141691862902792263L;

	private String rsrcId;			/** 자원ID */
	private String rsrcGroupCd;		/** 자원그룹코드(G:RSRC_GROUP) */
	private String rsrcTypeNm;		/** 자원유형명 */
	private String rsrcTypeUnitCd;	/** 자원유형단위코드(G:RSRC_TYPE_UNIT) */
	private Long invtQy;			/** 재고수량 */
	private Long rsrcCalcQy;		/** 자원연산량 */

	public int getInvtQy() {
		if (this.invtQy == null) {
			return 0;
		}
		return CoreUtils.number.toInt(this.invtQy.toString(), 0);
	}

	public int getRsrcCalcQy() {
		if (this.rsrcCalcQy == null) {
			return 0;
		}
		return CoreUtils.number.toInt(this.rsrcCalcQy.toString(), 0);
	}
}
