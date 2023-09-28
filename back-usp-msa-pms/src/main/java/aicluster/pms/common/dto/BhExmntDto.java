package aicluster.pms.common.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.pms.api.bsns.dto.BhExmntIem;
import lombok.Data;

@Data
public class BhExmntDto implements Serializable {
	private static final long serialVersionUID = 1650095673677490540L;
	/** 사전검토ID */
	private String bhExmntId;
	/** 사전검토구분명 */
	private String bhExmntDivNm;
	List<BhExmntIem> bhExmntIemList;
}
