package aicluster.pms.api.rslt.dto;

import java.io.Serializable;
import java.util.List;

import aicluster.framework.common.entity.CmmtAtchmnfl;
import aicluster.pms.common.dto.RsltIdxIemCnHistDto;
import lombok.Data;

@Data
public class RsltIdxIemHistDto implements Serializable {
	private static final long serialVersionUID = -2913421353173242722L;
	/** 성과지표항목ID */
	private String rsltIdxIemId;
	/** 성과지표ID */
	private String rsltIdxId;
	/** 성과지표명 */
	private String rsltIdxNm;
	/** 성과지표유형코드(G:RSLT_IDX_TYPE) */
	private String rsltIdxTypeCd;
	/** 증빙파일정보 */
	private CmmtAtchmnfl prufFile;
	/** 성과지표항목내용 목록 */
	List<RsltIdxIemCnHistDto> rsltIdxIemCnList;
}
