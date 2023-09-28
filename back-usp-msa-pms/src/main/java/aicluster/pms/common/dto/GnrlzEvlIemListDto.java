package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class GnrlzEvlIemListDto implements Serializable {
	private static final long serialVersionUID = 4107133839755206434L;
	/** 평가항목명 */
	private String evlIemNm;
	/** 평가내용 목록 */
	List<GnrlzEvlOpinionListDto> evlCnList;
}
