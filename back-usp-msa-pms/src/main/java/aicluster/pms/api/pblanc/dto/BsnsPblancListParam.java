package aicluster.pms.api.pblanc.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class BsnsPblancListParam implements Serializable {
	private static final long serialVersionUID = 20778322463970883L;
	/** 사업연도 */
	private String bsnsYear;
	/** 사업명 */
	private String bsnsNm;
	/** 공고명 */
	private String pblancNm;
	/** 접수상태 */
	private String pblancSttusCd;
	/** 접수시작일  */
	private String rceptBgndeStart;
	/** 접수시작일  */
	private String rceptBgndeEnd;
	/** 접수종료일  */
	private String rceptEnddeStart;
	/** 접수종료일  */
	private String rceptEnddeEnd;
	/** 공고일시작 */
	private String pblancDayStart;
	/** 공고일종료 */
	private String pblancDayEnd;
	/** 공고ID */
	private String pblancId;
	/** 포털게시상태 */
	private Boolean ntce;
	/** 엑셀 다운로드 여부 */
	private Boolean isExcel;
	/** 업무단계 */
	private String jobStepCd;
	/** 로그인 사용자 */
	private String insiderId;

	private Long beginRowNum;
	private Long itemsPerPage;
}
