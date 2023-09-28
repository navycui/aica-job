package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class SlctnPblancListDto implements Serializable {
	private static final long serialVersionUID = -3860746686917302307L;
	/** 게시여부 */
	private String ntce;
	/** 사업연도 */
	private String bsnsYear;
	/** 사업명 */
	private String bsnsNm;
	/** 선정공고ID */
	private String slctnPblancId;
	/** 공고명 */
	private String slctnPblancNm;
	/** 모집유형 */
	private String ordtmRcrit;
	/** 담당부서명 */
	private String chrgDeptNm;
	/** 공고일 */
	private String slctnPblancDay;
	/** 등록일 */
	private String regDt;
	/** 순번 */
	private int rn;
}
