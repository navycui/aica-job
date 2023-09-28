package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class BsnsApplyListDto implements Serializable {
	private static final long serialVersionUID = -2842306716036601381L;
	/** 접수ID */
	private String applyId;
	/** 접수상태 */
	private String rceptSttus;
	/** 접수번호(BA + 8자리 순번) */
	private String receiptNo;
	/** 접수차수 */
	private Integer rceptOdr;
	/** 사업연도 */
	private String bsnsYear;
	/** 사업명 */
	private String bsnsNm;
	/** 공고명 */
	private String pblancNm;
	/** 공고상태 */
	private String pblancSttus;
	/** 회원명 */
	private String memberNm;
	/** 과제명 */
	private String taskNm;
	/** 접수일시 */
	private String rceptDt;
	/** 순번 */
	private int rn;

}
