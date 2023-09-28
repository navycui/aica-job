package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class StdnListDto implements Serializable {
	private static final long serialVersionUID = 3604343301596330089L;
	/** 신청ID */
	private String applyId;
	/** 접수번호 */
	private String receiptNo;
	/** 회원명 */
	private String memberNm;
	/** 희망직무 */
	private String hopeDty;
	/** 공고명 */
	private String pblancNm;
	/** 모집유형 */
	private String ordtmRcrit;
	/** 접수차수*/
	private String rceptOdr;
	/** 순번 */
	private Long rn;
}
