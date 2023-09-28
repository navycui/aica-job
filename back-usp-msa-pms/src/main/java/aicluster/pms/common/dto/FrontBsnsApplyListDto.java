package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class FrontBsnsApplyListDto implements Serializable {
	private static final long serialVersionUID = 3785252150546984146L;
	/** 접수ID */
	private String applyId;
	/** 접수일시 */
	private String rceptDt;
	/** 접수상태코드(G:RCEPT_STTUS) */
	private String rceptSttusCd;
	/** 접수상태 */
	private String rceptSttus;
	/** 과제명 */
	private String taskNmKo;
	/** 공고ID */
	private String pblancId;
	/** 공고명 */
	private String pblancNm;
	/** 접수시작일  */
	private String rceptBgnde;
	/** 접수종료일  */
	private String rceptEndde;
	/** 접수마감시분 */
	private String rceptClosingHm;

}
