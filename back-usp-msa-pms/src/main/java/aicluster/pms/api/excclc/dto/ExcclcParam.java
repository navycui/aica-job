package aicluster.pms.api.excclc.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import lombok.Data;

@Data
public class ExcclcParam implements Serializable {
	private static final long serialVersionUID = -7784209757232262287L;
	/** 과제신청사업비ID */
	private String taskReqstWctId;
	/** 집행 보조금 */
	private Long excutSbsidy;
	/** 집행 사업자 부담금 */
	private Long excutBsnmAlotm;
	/** 집행 협약총액 */
	private Long excutCnvnTotamt;
	/** 삭제 첨부파일 목록 */
	List<CmmtAtchmnfl> deleteFileList;
}
