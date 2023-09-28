package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class StdrBsnsListDto implements Serializable {
	private static final long serialVersionUID = -7203802550961736184L;
	/** 기준사업코드 */
	private String stdrBsnsCd;
	/** 기준사업명 */
	private String stdrBsnsNm;
	/** 시작년도 */
	private String beginYear;
	/** 산업분류 */
	private String bsnsClNm;
	/** 담당부서명 */
	private String chrgDeptNm;
	/** 담당자명 */
	private String chargerNm;
	/** 담당자ID */
	private String insiderId;
	/** 등록일 */
	private String regDt;
	/** 순번 */
	private int rn;
}
