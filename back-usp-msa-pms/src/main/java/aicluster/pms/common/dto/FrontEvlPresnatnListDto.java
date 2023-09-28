package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import lombok.Data;

@Data
public class FrontEvlPresnatnListDto implements Serializable {

	/**
	 * 발료관리 조회 front
	 */
	private static final long serialVersionUID = -8751349626264159495L;

	/** 평가대상ID */
	String  evlTrgetId;
	/** 공고명 */
	String  pblancNm;
	/** 과제명(국문) */
	String  taskNmKo;
	/** 평가예정일시 */
	String evlPrarndt;
	/** 제출일 */
	String  fmtPresentnDt;
	/** 발표자료처리구분코드(G:PRESNATN_PROCESS_DIV) */
	String  presnatnProcessDivCd;
	/** 발표자료처리구분명 */
	String  presnatnProcessDivNm;
	/*첨부파일그룹ID*/
	String attachmentGroupId;

	/** 첨부파일 */
	List<CmmtAtchmnfl> attachFileList;
	/** 첨부파일삭제 List */
	List<CmmtAtchmnfl> attachFileDeleteList;
}
