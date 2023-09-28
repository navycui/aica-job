package aicluster.pms.api.evlresult.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.dto.FrontEvlOpinionListDto;
import lombok.Data;

@Data
public class FrontEvlResultDto implements Serializable {
	private static final long serialVersionUID = 4943109076161387858L;
	/** 공고명 */
	private String pblancNm;
	/** 선정여부 */
	private String slctnAt;
	/** 접수번호 */
	private String receiptNo;
	/** 과제명 */
	private String taskNmKo;
	/** 이의신청여부 */
	private String objcReqstAt;
	/** 이의신청가능여부 */
	private String objcReqstposblAt;
	/** 평가위원 의견 목록 */
	List<FrontEvlOpinionListDto> evlOpinionList;
}
