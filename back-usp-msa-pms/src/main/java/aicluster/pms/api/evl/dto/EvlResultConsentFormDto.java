package aicluster.pms.api.evl.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.dto.EvlAtendListDto;
import aicluster.pms.common.dto.EvlResultListDto;
import lombok.Data;

@Data
public class EvlResultConsentFormDto implements Serializable {
	private static final long serialVersionUID = -4004320312951797988L;
	/** 사업명 */
	private String bsnsNm;
	/** 공고명 */
	private String pblancNm;
	/** 평가단계명 */
	private String evlStepNm;
	/** 분과명 */
	private String sectNm;
	/** 평가예정일  */
	private String evlPrarnde;
	/** 평가장소 */
	private String evlPlace;
	/** 담당부서 */
	private String chrgDeptNm;
	/** 간사명 */
	private String orgnzrNm;

	/** 평가위원장종합의견내용 */
	private String evlCharmnOpinionCn;

	/** 선정결과 목록 */
	List<EvlResultListDto> slctnResultList;
	/** 평가위원 목록 */
	List<EvlAtendListDto> evlMfcmmList;
}
