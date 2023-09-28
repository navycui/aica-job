package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class BsnsListDto implements Serializable {
	private static final long serialVersionUID = -1178430404599185790L;
	/** 사업코드 */
	private String bsnsCd;
	/** 사업명 */
	private String bsnsNm;
	/** 기준사업코드 */
	private String stdrBsnsCd;
	/** 기준사업명 */
	private String stdrBsnsNm;
	/** 사업연도 */
	private String bsnsYear;
	/** 담당부서명 */
	private String chrgDeptNm;
	/** 담당자명 */
	private String chargerNm;
	/** 등록일 */
	private String regDt;
	/** 과제유형코드 (G: TASK_TYPE) */
	private String taskTypeCd;
	/** 과제유형명 */
	private String taskTypeNm;
	/** 순번 */
	private int rn;
	/**사업분류명 */
	private String bsnsClNm;
}
