package aicluster.pms.api.rslt.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.pms.common.dto.ApplcntBsnsBasicDto;
import lombok.Data;

@Data
public class RsltDto implements Serializable {
	private static final long serialVersionUID = 4194508595731775430L;
	/** 성과연도 */
	private String rsltYear;
	/** 성과상태코드(G:RSLT_STTUS) */
	private String rsltSttusCd;
	/** 성과상태 */
	private String rsltSttus;
	/** 기본정보 */
	private ApplcntBsnsBasicDto basicInfo;
	/** 첨부파일 */
	List<CmmtAtchmnfl> attachFileList;
	/** 성과지표항목 목록 */
	List<RsltIdxIemDto> rsltIdxIemList;
}
