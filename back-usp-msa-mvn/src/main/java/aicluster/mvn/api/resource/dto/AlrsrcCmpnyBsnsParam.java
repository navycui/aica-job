package aicluster.mvn.api.resource.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlrsrcCmpnyBsnsParam implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 5435382722635133492L;

	private String evlLastSlctnId;		/** 평가최종선정ID (USPT_BSNS_SLCTN.EVL_LAST_SLCTN_ID) */

	private List<AlrsrcCmpnyInsListItem> alrsrcCmpnyInsList;		/** 선정대상업체 입력목록 */
}
