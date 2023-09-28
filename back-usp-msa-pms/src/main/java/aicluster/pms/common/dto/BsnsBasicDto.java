package aicluster.pms.common.dto;

import java.util.List;

import aicluster.pms.common.entity.UsptBsns;
import aicluster.pms.common.entity.UsptBsnsCharger;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class BsnsBasicDto extends UsptBsns {
	private static final long serialVersionUID = -4407205519724425974L;
	/** 기준사업명 */
	private String stdrBsnsNm;
	/** 시작년도 */
	private String beginYear;
	/** 사업유형코드 */
	private String bsnsTypeCd;
	/** 사업유형명 */
	private String bsnsTypeNm;
	/** 사업개요 */
	private String bsnsSumry;
	/** 산업분류 */
	private String bsnsClNm;
	/** 추천분류 */
	List<RecomendClDto> bsnsRecomendClList;
	/** 담당자 */
	List<UsptBsnsCharger> bsnsChargerList;
}
