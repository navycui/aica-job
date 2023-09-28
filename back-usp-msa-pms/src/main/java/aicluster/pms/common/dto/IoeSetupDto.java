package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class IoeSetupDto implements Serializable {
	private static final long serialVersionUID = 2593022582868412611L;
	/** 사업비비목ID */
	private String wctIoeId;
	/** 사업비비목명 */
	private String wctIoeNm;
	/** 선택여부 */
	private Boolean isChecked;
	List<IoeSetupTaxitmDto> taxitmList;
}
