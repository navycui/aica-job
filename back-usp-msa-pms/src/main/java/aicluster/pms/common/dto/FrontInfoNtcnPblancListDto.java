package aicluster.pms.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class FrontInfoNtcnPblancListDto implements Serializable {
	private static final long serialVersionUID = 7115955095569211686L;
	/** 공고ID */
	private String pblancId;
	/** 공고명 */
	private String pblancNm;
	/** 공고요약 */
	private String pblancSumry;
	/** 마감남은일 */
	private Integer rmndrDay;
	/** 접수상태 */
	private String pblancSttus;
	/** 사업분야 */
	private String recomendCl;
	/** 이전공고ID */
	private String prePblancId;
	/** 다음공고ID */
	private String nextPblancId;
}
