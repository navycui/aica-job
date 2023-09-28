package aicluster.pms.api.rslt.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.pms.common.dto.ApplcntBsnsBasicDto;
import lombok.Data;

@Data
public class RsltHistDto implements Serializable {
	private static final long serialVersionUID = 940979533808825945L;
	/** 기본정보 */
	private ApplcntBsnsBasicDto basicInfo;
	/** 첨부파일 */
	List<CmmtAtchmnfl> attachFileList;
	/** 성과지표항목 목록 */
	List<RsltIdxIemHistDto> rsltIdxIemList;
}
