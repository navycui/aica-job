package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class BsnsPblancListDto implements Serializable {
	private static final long serialVersionUID = -6216178693282060722L;
	/** 접수상태 */
	private String pblancSttus;
	/** 사업연도 */
	private String bsnsYear;
	/** 사업코드 */
	private String bsnsCd;
	/** 사업명 */
	private String bsnsNm;
	/** 공고ID */
	private String pblancId;
	/** 공고명 */
	private String pblancNm;
	/** 접수차수 */
	private Integer rceptOdr;
	/** 상시모집여부 */
	private String ordtmRcrit;
	/** 접수시작일 */
	private String rceptBgnde;
	/** 접수종료일 */
	private String rceptEndde;
	/** 공고일 */
	private String pblancDay;
	/** 게시여부 */
	private String ntce;
	/** 순번 */
	private Long rn;
}
