package aicluster.pms.api.evl.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class EvlTargetListParam implements Serializable {
	private static final long serialVersionUID = 4683495197444155336L;
	/** 사업연도 */
	private String bsnsYear;
	/** 진행상태 - 평가상태코드(G:EVL_STTUS) */
	private String  evlSttusCd;
	/** 평가유형코드(G:EVL_TYPE) */
	private String  evlTypeCd;
	/** 통보상태
	 * (COMPT:통보완료, ING:대기중)
	 */
	private String dspthSttus;
	/** 평가방법
	 * (온라인:ON, 오프라인:OFF)
	 */
	private String evlMth;
	/** 키워드검색 */
	private String keyword;
	/** 키워드검색 구분
	 * (평가위원회명:evlCmitNm, 평가단계:evlStepNm, 분과명:sectNm, 간사명:orgnzrNm, 공고명: pblancNm)
	 */
	private String keywordDiv;
	/** 로그인한 내부자ID */
	private String insiderId;
	/** 평가위원회ID */
	private String evlCmitId;

	private Long beginRowNum;
	private Long itemsPerPage;
}
