package aicluster.pms.api.pblanc.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class FrontBsnsPblancListParam implements Serializable {
	private static final long serialVersionUID = -8253134689437012257L;
	/** 상시모집여부 */
	private Boolean ordtmRcrit;
	/** 접수상태 */
	List<String> pblancSttus;
	/** 지원가능회원유형코드 */
	List<String> applyMberType;
	/** 사업분야 분류코드 */
	List<String> recomendCl;
	/** 공고명 */
	private String pblancNm;

	/** 공고ID */
	private String pblancId;
	/**
	 * 정렬구분
	 * (pblancDay:공고일순, close:마감일순, rdcnt:조회순)
	 */
	private String sortOrder;

	private String isCloseing;

	private Long beginRowNum;
	private Long itemsPerPage;
}
