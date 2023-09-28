package aicluster.pms.api.evl.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.common.dto.GnrlzEvlIemListDto;
import aicluster.pms.common.dto.GnrlzEvlOpinionListDto;
import lombok.Data;

@Data
public class GnrlzEvlDto implements Serializable {
	private static final long serialVersionUID = -500858073286328035L;
	/** 접수번호 */
	private String receiptNo;
	/** 사용자명 */
	private String memberNm;
	/** 평가항목 목록 */
	List<GnrlzEvlIemListDto> evlIemList;
	/** 평가의견 목록 */
	List<GnrlzEvlOpinionListDto> evlOpinionList;
}
