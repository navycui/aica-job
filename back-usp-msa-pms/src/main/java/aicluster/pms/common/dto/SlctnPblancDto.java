package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;import aicluster.pms.common.entity.UsptSlctnPblancDetail;
import lombok.Data;

@Data
public class SlctnPblancDto implements Serializable {
	private static final long serialVersionUID = -4064992108626116996L;
	/** 선정공고ID */
	private String slctnPblancId;
	/** 공고ID */
	private String pblancId;
	/** 접수차수 */
	private Integer rceptOdr;
	/** 분과ID */
	private String sectId;
	/** 평가단계ID */
	private String evlStepId;
	/** 평가최종선정ID */
	private String evlLastSlctnId;
	/** 선정공고명 */
	private String slctnPblancNm;
	/** 선정공고번호 */
	private String slctnPblancNo;
	/** 선정공고일 */
	private String slctnPblancDay;
	/** 선정공고요약 */
	private String slctnPblancSumry;
	/** 사용여부 */
	private Boolean enabled;
	/** 게시여부 */
	private Boolean ntce;
	/** 게시여부 */
	private String ntceNm;
	/** 등록일 */
	private String regDt;
	/** 사업연도 */
	private String bsnsYear;
	/** 사업명 */
	private String bsnsNm;
	/** 공고명 */
	private String pblancNm;
	/** 모집유형 */
	private Boolean ordtmRcrit;
	/** 모집유형명 */
	private String ordtmRcritNm;
	/** 첨부파일그룹ID */
	private String attachmentGroupId;
	/** 담당자권한코드 */
	private String chargerAuthorCd;
	/** 공고대상 */
	EvlPlanPblancStepDto pblancTarget;
	/** 상세목록 */
	List<UsptSlctnPblancDetail> detailList;
	/** 첨부파일 목록 */
	List<CmmtAtchmnfl> attachFileList;
}
