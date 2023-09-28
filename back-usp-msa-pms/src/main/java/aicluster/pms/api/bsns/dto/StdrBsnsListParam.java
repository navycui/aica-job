package aicluster.pms.api.bsns.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class StdrBsnsListParam implements Serializable {

	private static final long serialVersionUID = 6579152409948908670L;

	/** 대분류 */
	private String parentBsnsClId;
	/** 소분류 */
	private String bsnsClId;
	/** 기준사업코드 */
	private String stdrBsnsCd;
	/** 기준사업명 */
	private String stdrBsnsNm;
	/** 담당자명 */
	private String chargerNm;
	/** 담당부서 */
	private String chrgDeptNm;
	/** 엑셀 다운로드 여부 */
	private Boolean isExcel;

	private Long beginRowNum;
	private Long itemsPerPage;

}
